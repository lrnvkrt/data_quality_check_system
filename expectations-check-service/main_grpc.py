import grpc
import logging
import signal
from concurrent import futures
from app.grpc import validation_pb2_grpc, validation_pb2
from app.service.validator import validate_with_expectations

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(name)s: %(message)s")


class ValidationServicer(validation_pb2_grpc.ValidationServiceServicer):
    def Validate(self, request, context):
        logger.debug("Incoming request:\n%s", request)
        try:
            return validate_with_expectations(request)
        except ValueError as e:
            logger.warning("Validation error: %s", e)
            context.set_details(str(e))
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            return validation_pb2.ValidationResponse(success=False)
        except Exception as e:
            logger.exception("Internal error during validation")
            context.set_details("Internal error: " + str(e))
            context.set_code(grpc.StatusCode.INTERNAL)
            return validation_pb2.ValidationResponse(success=False)


def create_server() -> grpc.Server:
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=4))
    validation_pb2_grpc.add_ValidationServiceServicer_to_server(ValidationServicer(), server)
    server.add_insecure_port('[::]:50051')
    return server


def setup_graceful_shutdown(server: grpc.Server):
    def shutdown_handler(signum, frame):
        logger.info("Received shutdown signal. Stopping gRPC server...")
        server.stop(0)

    signal.signal(signal.SIGINT, shutdown_handler)
    signal.signal(signal.SIGTERM, shutdown_handler)


def serve():
    server = create_server()
    setup_graceful_shutdown(server)

    server.start()
    logger.info("gRPC server started on port 50051")
    server.wait_for_termination()


if __name__ == '__main__':
    logger.info("Starting validation gRPC service...")
    serve()