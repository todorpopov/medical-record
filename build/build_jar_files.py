import logging
import argparse
import os
import subprocess
import sys

class InvalidModuleError(BaseException):
    def __init__(self, message, errors):
        super().__init__(message)
        self.errors = errors

class BuildJarProcessResponse:
    def __init__(self, is_successful, module_name, errors):
        self.is_successful = is_successful
        self.module_name = module_name
        self.errors = errors


class JarFileBuilder:
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

    def build_single_jar(self, path):
        module_name = os.path.basename(path)

        cmd = ["mvn", "clean", "package"]

        self._logger.info(f"Building jar for module '{module_name}' in '{path}")
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

            return BuildJarProcessResponse(True, module_name, None)
        except subprocess.CalledProcessError as e:
            return BuildJarProcessResponse(False, module_name, e.stdout)

    def main(self):
        parser = argparse.ArgumentParser(description="Building Maven sub-module jar files")
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

            result = self.build_single_jar(module_path)

            if not result.is_successful:
                self._logger.error(f"Failed to build module jar '{result.module_name}: {result.errors}")
                fail_count += 1
            else:
                self._logger.info(f"Jar file for module '{result.module_name}' has been built successfully!")
                success_count += 1

        self._logger.info(f"Successful builds: {success_count} | Failed builds: {fail_count}")
        return self.SUCCESS_STATUS


if __name__ == "__main__":
    obj = JarFileBuilder()
    sys.exit(obj.main())
