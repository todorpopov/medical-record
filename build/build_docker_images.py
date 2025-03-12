import logging
import argparse
import os
import subprocess
import sys

class InvalidModuleError(BaseException):
    def __init__(self, message, errors):
        super().__init__(message)
        self.errors = errors

class DockerBuildProcessResponse:
    def __init__(self, is_successful, module_name, errors):
        self.is_successful = is_successful
        self.module_name = module_name
        self.errors = errors


class DockerImageBuilder:
    SUCCESS_STATUS = 0
    ERROR_STATUS = 1

    BASE_DIR = os.path.dirname(os.path.dirname(__file__))

    def __init__(self):
        logging.basicConfig(
            level=logging.INFO,
            format='%(asctime)s - %(levelname)s - %(message)s',
            datefmt='%Y-%m-%d %H:%M:%S'
        )

        self._logger = logging.getLogger(__name__)

    def build_docker_image(self, module_name, path):
        cmd = ["docker", "build", "-t", f"{module_name}-service:latest", f"{path}"]

        self._logger.info(f"Building Docker image for module '{module_name}' in '{path}")
        self._logger.info(f"Executing command: {cmd}")

        try:
            subprocess.run(
                cmd,
                cwd=path,
                check=True,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                text=True
            )

            return DockerBuildProcessResponse(True, module_name, None)
        except subprocess.CalledProcessError as e:
            return DockerBuildProcessResponse(False, module_name, e.stderr)

    def main(self):
        parser = argparse.ArgumentParser(description="Building Docker images")
        parser.add_argument("--modules", nargs='+', help="Specific modules to build. Separate modules by a single space!")

        args = parser.parse_args()

        correct_modules = ["api", "appointments", "auth", "diagnoses", "users"]
        valid_modules = []

        if args.modules is None:
            valid_modules = correct_modules
        else:
            for module in args.modules:
                if module not in correct_modules:
                    self._logger.info(f"Module '{module}' is not correct!")
                else:
                    valid_modules.append(module)

        success_count = 0
        fail_count = 0

        for module in valid_modules:
            module_path = os.path.join(self.BASE_DIR, f"{module}")

            result = self.build_docker_image(module, module_path)

            if not result.is_successful:
                self._logger.error(f"Failed to build Docker image '{result.module_name}: {result.errors}")
                fail_count += 1
            else:
                self._logger.info(f"Docker image for module '{result.module_name}' has been built successfully!")
                success_count += 1

        self._logger.info(f"Successful builds: {success_count} | Failed builds: {fail_count}")
        return self.SUCCESS_STATUS


if __name__ == "__main__":
    obj = DockerImageBuilder()
    sys.exit(obj.main())
