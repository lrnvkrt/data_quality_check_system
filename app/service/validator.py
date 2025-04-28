import pandas as pd
import logging
from great_expectations.validator.validator import Validator
from great_expectations.core.expectation_suite import ExpectationSuite
from great_expectations.execution_engine import PandasExecutionEngine
from great_expectations.core.batch import Batch, BatchDefinition, BatchSpec, BatchMarkers
from great_expectations.core.id_dict import IDDict
from app.grpc import validation_pb2

logger = logging.getLogger(__name__)
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s"
)

def construct_dataframe(request) -> pd.DataFrame:
    try:
        logger.info("Starting to construct DataFrame from request")
        rows = []
        for record in request.data:
            row = {k: unpack_proto_value(v) for k, v in record.fields.items()}
            rows.append(row)
        df = pd.DataFrame(rows)
        logger.info("DataFrame constructed: %d rows, %d columns", df.shape[0], df.shape[1])
        logger.debug("DataFrame preview:\n%s", df.head())
        return df
    except Exception:
        logger.exception("Failed to construct DataFrame")
        raise

def unpack_proto_value(proto_value) -> object:
    kind = proto_value.WhichOneof("value")
    if kind == "list_value":
        return [unpack_proto_value(v) for v in getattr(proto_value, kind).values]
    if kind == "struct_value":
        return {k: unpack_proto_value(v) for k, v in getattr(proto_value, kind).fields.items()}
    return getattr(proto_value, kind)

def parse_kwargs(kwarg_dict: dict) -> dict:
    return {k: unpack_proto_value(v) for k, v in kwarg_dict.items()}

def initialize_validator(df) -> Validator:
    try:
        logger.info("Initializing Great Expectations Validator")
        engine = PandasExecutionEngine()
        engine.load_batch_data("default_batch", df)

        batch_definition = BatchDefinition(
            datasource_name="manual_datasource",
            data_connector_name="manual_connector",
            data_asset_name="default_asset",
            batch_identifiers=IDDict({})
        )

        batch = Batch(
            data=df,
            batch_request=None,
            batch_definition=batch_definition,
            batch_spec=BatchSpec({"batch_id": "default_batch"}),
            batch_markers=BatchMarkers({"ge_load_time": "now"})
        )

        validator = Validator(execution_engine=engine, batch=batch)
        logger.info("Validator successfully initialized")
        return validator
    except Exception as e:
        logger.exception("Failed to initialize Validator")
        raise

def apply_expectations(validator, expectations) -> None:
    logger.info("Applying %d expectations", len(expectations))
    for e in expectations:
        expectation_type = e.expectation_type
        kwargs = {k: unpack_proto_value(v) for k, v in e.kwargs.items()}
        meta = dict(e.meta)

        logger.debug("Applying expectation: %s", expectation_type)
        logger.debug("Kwargs: %s", kwargs)
        logger.debug("Meta: %s", meta)
        logger.debug("Row condition raw: %r", kwargs.get("row_condition"))
        logger.debug("Regex raw: %r", kwargs.get("regex"))

        try:
            method = getattr(validator, expectation_type)
            method(**kwargs, meta=meta)
        except AttributeError:
            logger.warning("Unsupported expectation type: %s", expectation_type)
        except Exception as err:
            logger.exception("Failed to apply expectation: %s", expectation_type)

def validate_data(validator) -> dict:
    try:
        logger.info("Starting data validation")
        validation_result = validator.validate()
        logger.info("Validation completed")
        return validation_result
    except Exception as e:
        logger.exception("Validator.validate() failed")
        raise

def assemble_response(result: dict) -> validation_pb2.ExpectationResult:
    try:
        logger.info("Assembling ValidationResponse")
        stats = result["statistics"]

        response = validation_pb2.ValidationResponse(
            success=result["success"],
            evaluated_expectations=stats["evaluated_expectations"],
            successful_expectations=stats["successful_expectations"],
            unsuccessful_expectations=stats["unsuccessful_expectations"]
        )

        seen = set()
        for r in result["results"]:
            expectation_type = r["expectation_config"]["expectation_type"]
            column = r["expectation_config"]["kwargs"].get("column", "")
            kwargs_key = (expectation_type, column)

            if kwargs_key in seen:
                continue
            seen.add(kwargs_key)

            success = r["success"]

            if not success:
                unexpected_rows = r.get("result", {}).get("unexpected_index_list", [])
                unexpected_values = r.get("result", {}).get("unexpected_list", [])
                error_message = f"Column '{column}' failed '{expectation_type}' at rows {unexpected_rows} (values: {unexpected_values})"
            else:
                error_message = ""
                unexpected_rows = []

            response.results.append(validation_pb2.ExpectationResult(
                expectation_type=expectation_type,
                column=column,
                success=bool(success),
                error_message=error_message,
                failed_row_indices=unexpected_rows
            ))

        logger.info("ValidationResponse built. Success: %d, Failure: %d",
                    stats["successful_expectations"], stats["unsuccessful_expectations"])
        return response
    except Exception as e:
        logger.exception("Failed to assemble ValidationResponse")
        raise

def validate_with_expectations(request) -> validation_pb2.ValidationResponse:
    logger.info("START validation")
    logger.info("Incoming records count: %d", len(request.data))
    logger.info("Suite name: %s", request.expectation_suite.expectation_suite_name)

    if not request.data:
        logger.warning("Empty request - returning success without validation")
        return validation_pb2.ValidationResponse(success=True)

    df = construct_dataframe(request)
    validator = initialize_validator(df)
    apply_expectations(validator, request.expectation_suite.expectations)
    validation_result = validate_data(validator)
    response = assemble_response(validation_result)

    logger.debug("Final gRPC response:\n%s", response)
    logger.info("Validation process completed. Success: %s", response.success)

    return response
