import pytest
import pandas as pd
from unittest.mock import MagicMock
from app.service.validator import (
    construct_dataframe,
    unpack_proto_value,
    initialize_validator,
    apply_expectations,
    validate_with_expectations,
)
from app.grpc import validation_pb2

class DummyValue:
    def __init__(self, value=None, list_value=None, struct_value=None):
        self.int_value = value
        self.double_value = value
        self.string_value = value
        self.bool_value = value
        self.list_value = list_value
        self.struct_value = struct_value

    def WhichOneof(self, _):
        if self.list_value is not None:
            return "list_value"
        if self.struct_value is not None:
            return "struct_value"
        if isinstance(self.int_value, int):
            return "int_value"
        if isinstance(self.double_value, float):
            return "double_value"
        if isinstance(self.string_value, str):
            return "string_value"
        if isinstance(self.bool_value, bool):
            return "bool_value"

    def __getattr__(self, item):
        return self.__dict__.get(item)

def test_unpack_proto_value_scalar():
    val = DummyValue(value=42)
    assert unpack_proto_value(val) == 42

def test_unpack_proto_value_list():
    val = DummyValue(list_value=MagicMock(values=[DummyValue(value="a"), DummyValue(value="b")]))
    assert unpack_proto_value(val) == ["a", "b"]

def test_unpack_proto_value_struct():
    struct = DummyValue(struct_value=MagicMock(fields={
        "key": DummyValue(value=123)
    }))
    assert unpack_proto_value(struct) == {"key": 123}

def test_construct_dataframe():
    request = MagicMock()
    request.data = [
        MagicMock(fields={"a": DummyValue(value=1), "b": DummyValue(value="x")}),
        MagicMock(fields={"a": DummyValue(value=2), "b": DummyValue(value="y")})
    ]
    df = construct_dataframe(request)
    assert isinstance(df, pd.DataFrame)
    assert df.shape == (2, 2)
    assert list(df.columns) == ["a", "b"]

def test_initialize_validator():
    df = pd.DataFrame({"col1": [1, 2, 3]})
    validator = initialize_validator(df)
    assert validator is not None
    assert hasattr(validator, "expect_table_row_count_to_be_between")

def test_apply_expectations_calls_validator_methods(monkeypatch):
    validator = MagicMock()
    validator.expect_column_values_to_not_be_null = MagicMock()
    
    expectation = MagicMock()
    expectation.expectation_type = "expect_column_values_to_not_be_null"
    expectation.kwargs = {"column": DummyValue(value="test_column")}
    expectation.meta = {}

    apply_expectations(validator, [expectation])
    validator.expect_column_values_to_not_be_null.assert_called_once_with(column="test_column", meta={})
