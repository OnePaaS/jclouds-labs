/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.docker.features;

import org.jclouds.docker.DockerApi;
import org.jclouds.docker.compute.BaseDockerApiLiveTest;
import org.jclouds.docker.options.CreateImageOptions;
import org.jclouds.providers.AnonymousProviderMetadata;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.providers.Providers;
import org.testng.annotations.Test;

import java.io.*;
import java.util.NoSuchElementException;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@Test(groups = "live", testName = "RemoteApiLiveTest", singleThreaded = true)
public class ImageApiLiveTest extends BaseDockerApiLiveTest {

   private static final String DEFAULT_IMAGE = "hello-world";
   private static final String DEFAULT_TAG = "latest";
   private static String MESSAGE = "";


//   @BeforeClass(
//           groups = {"integration", "live"}
//   )
//   public void setup() {
//      System.out.println("aaaaaaa ................");
//      //拷贝服务
//      File srcFile = new File("D:\\opensource\\jclouds-labs\\docker\\target\\services\\");
//      File destFile = new File("D:\\opensource\\jclouds-labs\\docker\\target\\classes\\META-INF");
//      try{
//         copyFolder(srcFile, destFile);
//      }catch (IOException e){
//         System.out.println(e.getMessage());
//      }
//      this.initialize();
//   }

   protected ProviderMetadata createProviderMetadata() {
      System.out.println("bbbbbbb ................");
      try {
         return Providers.withId(this.provider);
      } catch (NoSuchElementException var2) {
         ProviderMetadata test = AnonymousProviderMetadata.forApiOnEndpoint(DockerApi.class, "http://192.168.16.14:8888");
         return test;
      }
   }

//   protected ContextBuilder newBuilder() {
//      System.out.println("ccccccc ................");
//      if(this.provider != null) {
//         try {
//            return ContextBuilder.newBuilder(this.provider);
//         } catch (NoSuchElementException var3) {
//            Logger.getAnonymousLogger().warning("provider [" + this.provider + "] is not setup as META-INF/services/org.jclouds.apis.ApiMetadata or META-INF/services/org.jclouds.providers.ProviderMetadata");
//         }
//      }
//
//      ProviderMetadata pm = this.createProviderMetadata();
//      ContextBuilder builder = pm != null?ContextBuilder.newBuilder(pm):ContextBuilder.newBuilder((ApiMetadata)ApiMetadata.class.cast(Preconditions.checkNotNull(this.createApiMetadata(), "either createApiMetadata or createProviderMetadata must be overridden")));
//      return builder;
//   }

   @Test
   public void testCreateImage() {
      InputStream createImageStream = api().createImage(CreateImageOptions.Builder.fromImage(DEFAULT_IMAGE).tag(DEFAULT_TAG));
      consumeStream(createImageStream);
   }

//   @Test(dependsOnMethods = "testCreateImage")
//   public void testInspectImage() {
//      assertNotNull(api.getImageApi().inspectImage(String.format("%s:%s", DEFAULT_IMAGE, DEFAULT_TAG)));
//   }

   @Test()
   public void testListImages() {
      assertNotNull(api().listImages());
   }

   @Test(dependsOnMethods = "testListImages")
   public void testDeleteImage() {
      consumeStream(api().deleteImage(String.format("%s:%s", DEFAULT_IMAGE, DEFAULT_TAG)));
      assertNull(api().inspectImage(String.format("%s:%s", DEFAULT_IMAGE, DEFAULT_TAG)));
   }

   private ImageApi api() {
      return api.getImageApi();
   }

   protected static void  copyFolder(File src, File dest) throws IOException {
      if (src.isDirectory()) {
         if (!dest.exists()) {
            dest.mkdir();
         }
         String files[] = src.list();
         for (String file : files) {
            File srcFile = new File(src, file);
            File destFile = new File(dest, file);
            // 递归复制
            copyFolder(srcFile, destFile);
         }
      } else {
         InputStream in = new FileInputStream(src);
         OutputStream out = new FileOutputStream(dest);

         byte[] buffer = new byte[1024];

         int length;

         while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
         }
         in.close();
         out.close();
      }
   }
}
