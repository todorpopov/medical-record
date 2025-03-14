from abstract_builders.abstract_module_builder import AbstractModuleBuilder, BuildProcessResponse

import os
import subprocess
import sys

class JarFileBuilder(AbstractModuleBuilder):
    BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(__file__)))

    def __init__(self, init_modules=None):
        super().__init__(["api", "appointments", "auth", "diagnoses", "users"], init_modules)

        sys.exit(self.main())

    def run_build_module_command(self, path):
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

            return BuildProcessResponse(True, module_name, None)
        except subprocess.CalledProcessError as e:
            return BuildProcessResponse(False, module_name, e.stdout)

    def main(self):
        modules = self._get_modules()

        success_count = 0
        fail_count = 0

        for module in modules:
            module_path = os.path.join(self.BASE_DIR, f"{module}")

            result = self.run_build_module_command(module_path)

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
