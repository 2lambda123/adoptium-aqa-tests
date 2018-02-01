<!--
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
-->

# How-to Run JCK Tests

* Prerequisites:
  * OpenJDK Community TCK License Agreement (OCTLA)
  * your own set of JCK test materials (JCK test source under OCTLA License): jck8b or jck9
  * ant 1.10.1 or above with ant-contrib.jar


1. Put unarchived jck test materials (jck8b or jck9) into an empty folder, for example:
* `/jck/jck8b/` and `/jck/jck9`

2. Export `JCK_ROOT=/jck` as an environment variable or pass it in makefile when run make commands

3. Export `JCK_VERSION=<your_jck_version>` as an environment variable or pass it in makefile when run make commands. For example `export JCK_VERSION=jck8b` 

4. Export `JAVA_HOME=<your_JDK_root>` as an environment variable

5. The other steps will stay the same as instructed in `openjdk-tests/README.md`


This test directory contains:
  * build.xml file - that clones AdoptOpenJDK/stf repo to pick up a test framework
  * playlist.xml - to allow easy inclusion of JCK tests into automated builds


# How-to Run customized JCK test targets

There are three custom JCK test targets `jck-runtime-custom`, `jck-compiler-custom` and `jck-devtools-custom`. With these three test targets, you can run custom JCK subsets.

1. Follow the Steps 1 - 4 mentioned above. 

2. Export `JCK_TEST_TARGET=<jck_test_subset>` as an environment variable or pass it in makefile when run make commands. For example `export JCK_TEST_TARGET=api/java_math`

3. Make sure the JCK test subset is available in JCK test material folder, a.k.a. `$(JCK_ROOT)/$(JCK_VERSION)/`.

4. Follow the steps remaining in `openjdk-tests/README.md`