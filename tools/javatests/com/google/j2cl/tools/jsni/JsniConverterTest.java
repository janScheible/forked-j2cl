/*
 * Copyright 2015 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */
package com.google.j2cl.tools.jsni;

import static com.google.common.truth.Truth.assertThat;
import static com.google.j2cl.tools.jsni.ToolsTestUtils.getDataFilePaths;

import com.google.j2cl.common.PackageInfoCache;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class tests:
 * <ul>
 *   <li>that the converter throws the right exception if the java file to convert contains things
 *   that the converter cannot handle;</li>
 *   <li>cases that are impossible to tests with an integration test.</li>
 * </ul>
 */
@RunWith(JUnit4.class)
public class JsniConverterTest extends BaseJsniConverterTest {

  @After
  public void tearDown() {
    PackageInfoCache.clear();
  }

  /**
   * The zip files generated by the converter should contains one javascript file for each java
   * classes containing native methods. We don't validate the content of the javascript files, this
   * kind of check is done in the integration tests.
   */
  @Test
  public void convert_severalJavaFile() throws IOException {
    new JsniConverter(getZipFilePath())
        .convert(
            getDataFilePaths("SimpleClass.java", "Outer.java", "NoNativeMethod.java"),
            new ArrayList<>(),
            new HashSet<>());

    // Collect content of the zip file.
    ZipInputStream zis = new ZipInputStream(new FileInputStream(getZipFile()));
    Set<String> jsFiles = new HashSet<>();
    ZipEntry entry;
    while ((entry = zis.getNextEntry()) != null) {
      jsFiles.add(entry.getName());
    }

    assertThat(jsFiles).hasSize(3);
    assertThat(jsFiles)
        .containsExactly(
            "com/foo/SimpleClass.native.js",
            "com/foo/Outer$Inner.native.js",
            "com/foo/Outer$Inner$InnerInner.native.js");
  }

  /**
   * If the java file passed to the converter doesn't contain any native method, the zip file is
   * empty and the converter doesn't throw any errors.
   */
  @Test
  public void convert_javaFileWithoutNativeMethod() throws IOException {
    new JsniConverter(getZipFilePath())
        .convert(getDataFilePaths("NoNativeMethod.java"), new ArrayList<>(), new HashSet<>());

    // check the content of the zip file is empty
    ZipInputStream zis = new ZipInputStream(new FileInputStream(getZipFile()));
    assertThat(zis.getNextEntry()).isNull();
  }

  /**
   * If the body of a jsni method contain a jsni reference to an instance java method, the converter
   * throws a RuntimeException
   */
  @Test(expected = RuntimeException.class)
  public void convert_nativeMethodWithJsniReferenceToInstanceMethod() {
    new JsniConverter(getZipFilePath())
        .convert(getDataFilePaths("MethodJsniReference.java"), new ArrayList<>(), new HashSet<>());
  }

  /**
   * If the body of a jsni method contain a jsni reference to a static java method, the converter
   * throws a RuntimeException
   */
  @Test(expected = RuntimeException.class)
  public void convert_nativeMethodWithJsniReferenceToStaticMethod() {
    new JsniConverter(getZipFilePath())
        .convert(
            getDataFilePaths("StaticMethodJsniReference.java"), new ArrayList<>(), new HashSet<>());
  }

  /**
   * If the body of a jsni method contain a jsni reference to an instance java field, the converter
   * throws a RuntimeException
   */
  @Test(expected = RuntimeException.class)
  public void convert_nativeMethodWithJsniReferenceToInstanceField() {
    new JsniConverter(getZipFilePath())
        .convert(
            getDataFilePaths("InstanceFieldJsniReference.java"),
            new ArrayList<>(),
            new HashSet<>());
  }

  /**
   * If the body of a jsni method contain a jsni reference to a static java field, the converter
   * throws a RuntimeException
   */
  @Test(expected = RuntimeException.class)
  public void convert_nativeMethodWithJsniReferenceToStaticField() {
    new JsniConverter(getZipFilePath())
        .convert(
            getDataFilePaths("StaticFieldJsniReference.java"), new ArrayList<>(), new HashSet<>());
  }

  /**
   * If the body of native method doesn't use the jsni block comment pattern, the converter will
   * skip it.
   */
  @Test
  public void convert_noWellFormedJsniBlock() throws IOException {
    new JsniConverter(getZipFilePath())
        .convert(
            getDataFilePaths("NotWellFormedNativeMethod.java"), new ArrayList<>(), new HashSet<>());

    // check the content of the zip file is empty
    ZipInputStream zis = new ZipInputStream(new FileInputStream(getZipFile()));
    assertThat(zis.getNextEntry()).isNull();
  }
}
