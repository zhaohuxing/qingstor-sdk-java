// +-------------------------------------------------------------------------
// | Copyright (C) 2016 Yunify, Inc.
// +-------------------------------------------------------------------------
// | Licensed under the Apache License, Version 2.0 (the "License");
// | you may not use this work except in compliance with the License.
// | You may obtain a copy of the License in the LICENSE file, or at:
// |
// | http://www.apache.org/licenses/LICENSE-2.0
// |
// | Unless required by applicable law or agreed to in writing, software
// | distributed under the License is distributed on an "AS IS" BASIS,
// | WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// | See the License for the specific language governing permissions and
// | limitations under the License.
// +-------------------------------------------------------------------------

package scenario_impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.qingstor.sdk.config.EvnContext;
import com.qingstor.sdk.constants.QSConstant;
import com.qingstor.sdk.service.Bucket;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ObjectTest {


	private static Bucket subService;
    private static String bucketName = TestUtil.getBucketName();
    
    private static String test_object = "";
    private static String test_object_copy = "";
    private static String test_object_move = "";


    private static Bucket.PutObjectOutput objectOutput;
    private static Bucket.PutObjectOutput copyOutput;
    private static Bucket.PutObjectOutput moveOutput;
    private static Bucket.GetObjectOutput getContentTypeOutput;

    private static Bucket.PutObjectOutput putObjectOutput;
    private static Bucket.GetObjectOutput getObjectOutput;
    private static Bucket.HeadObjectOutput headObjectOutput;
    private static Bucket.OptionsObjectOutput optionObjectOutput;
    private static Bucket.DeleteObjectOutput deleteObjectOutput;
    private static Bucket.DeleteObjectOutput deleteObjectOutput2;

    @When("^initialize the object with key \"([^\"]*)\"$")
    public void initialize_the_object_with_key(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        EvnContext evnContext = TestUtil.getEvnContext();
        subService = new Bucket(evnContext,bucketName);
        evnContext.setLog_level(QSConstant.LOGGER_INFO);
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        //objectOutput = subService.PutObject(statueCode,input);
        test_object =objectKey;

    }

    @Then("^the object is initialized$")
    public void the_object_is_initialized() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        TestUtil.assertNotNull(this.subService);
    }

    @When("^initialize the copy object with key \"([^\"]*)\"$")
    public void initialize_the_copy_object_with_key(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        copyOutput = subService.putObject(objectKey,input);
        test_object_copy = objectKey;
    }

    @Then("^the copy object is initialized$")
    public void the_copy_object_is_initialized() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        ///TestUtil.assertEqual(copyOutput.getStatueCode(),);
        System.out.println("the_copy_object_is_initialized:"+test_object_copy);
    }

    @When("^initialize the move object with key \"([^\"]*)\"$")
    public void initialize_the_move_object_with_key(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        moveOutput = subService.putObject(objectKey,input);
        test_object_move = objectKey;
    }

    @Then("^the move object is initialized$")
    public void the_move_object_is_initialized() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //
        System.out.println("the_move_object_is_initialized:"+test_object_move);
    }

    @When("^put object with key \"([^\"]*)\"$")
    public void put_object_with_key(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        EvnContext evnContext = TestUtil.getEvnContext();
        evnContext.setLog_level(QSConstant.LOGGER_INFO);
        subService = new Bucket(evnContext,bucketName);
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        File f = new File("config.yaml");
        input.setBodyInputFile(f);
        input.setContentType("video/mp4; charset=utf8");
        input.setContentLength( f.length());
        this.test_object = objectKey;
        putObjectOutput = subService.putObject(test_object,input);
    }

    @Then("^put object status code is (\\d+)$")
    public void put_object_status_code_is(int statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("put_object_status_code_msg:"+this.putObjectOutput.getMessage()+test_object);
        TestUtil.assertEqual(putObjectOutput.getStatueCode(),statueCode);
    }

    @When("^copy object with key \"([^\"]*)\"$")
    public void copy_object_with_key(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        input.setXQSCopySource("/" + bucketName + "/" + this.test_object);
        this.test_object_copy = objectKey+"copy";
        copyOutput = subService.putObject(test_object_copy,input);
    }

    @Then("^copy object status code is (\\d+)$")
    public void copy_object_status_code_is(int statueCode)  throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //
        System.out.println("put_the_copy_object_status_code_message:"+this.test_object+copyOutput.getMessage()+test_object_copy);
        TestUtil.assertEqual(copyOutput.getStatueCode(),statueCode);
    }

    @When("^move object with key \"([^\"]*)\"$")
    public void move_object_with_key(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        this.test_object_move = objectKey+"move";
        input.setXQSMoveSource("/" + bucketName + "/" + test_object_copy);
        moveOutput = subService.putObject(test_object_move,input);
    }

    @Then("^move object status code is (\\d+)$")
    public void move_object_status_code_is(int statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //
        System.out.println("put_the_move_object_status_code_message:"+copyOutput.getMessage());
        TestUtil.assertEqual(moveOutput.getStatueCode(),statueCode);
    }

    @When("^get object with key \"([^\"]*)\"$")
    public void get_object(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Bucket.GetObjectInput input = new Bucket.GetObjectInput();
        getObjectOutput = subService.getObject(objectKey+"move",input);
    }

    @Then("^get object status code is (\\d+)$")
    public void get_object_status_code_is(int statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        TestUtil.assertEqual(getObjectOutput.getStatueCode(),statueCode);
    }

    @Then("^get object content length is (\\d+)$")
    public void get_object_content_length_is(int statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        int iLength = 0;
        if(getObjectOutput != null && getObjectOutput.getBodyInputStream() != null){
            File ff = new File("/tmp/get_object.txt");
            OutputStream out = new FileOutputStream(ff);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = getObjectOutput.getBodyInputStream().read(buffer,0,1024)) != -1){
                out.write(buffer,0,bytesRead);
                iLength += bytesRead;
            }
            out.close();
            getObjectOutput.getBodyInputStream().close();
        }
        //TestUtil.assertEqual(iLength,statueCode);
    }


    @When("^get object \"([^\"]*)\" with query signature$")
    public void get_object_with_query_signature(String statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //
        String reqUrl = subService.GetObjectSignatureUrl(statueCode,1000);
        getObjectOutput = subService.GetObjectBySignatureUrl(reqUrl);

    }

    @Then("^get object with query signature content length is (\\d+)$")
    public void get_object_with_query_signature_content_length_is(int statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //
        System.out.println("get_object_with_query_signature_statue:"+getObjectOutput.getStatueCode());
        int iLength = 0;
        if(getObjectOutput != null && getObjectOutput.getBodyInputStream() != null){
            File ff = new File("/tmp/get_sign_object.txt");
            OutputStream out = new FileOutputStream(ff);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = getObjectOutput.getBodyInputStream().read(buffer,0,1024)) != -1){
                out.write(buffer,0,bytesRead);
                iLength += bytesRead;
            }
            out.close();
            getObjectOutput.getBodyInputStream().close();
        }
        System.out.println("get_object_with_query_signature_length:"+iLength);
    }

    @When("^get object with content type \"([^\"]*)\"$")
    public void get_object_with_content_type(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Bucket.GetObjectInput input = new Bucket.GetObjectInput();
        input.setResponseContentType(objectKey);
        getContentTypeOutput = subService.getObject(test_object, input);
    }

    @Then("^get object content type is \"([^\"]*)\"$")
    public void get_object_content_type_is(String statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        TestUtil.assertEqual(statueCode,getContentTypeOutput.getResponseContentType());
    }


    @When("^head object with key \"([^\"]*)\"$")
    public void head_object(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Bucket.HeadObjectInput input = new Bucket.HeadObjectInput();

        headObjectOutput = subService.headObject(objectKey,input);
    }

    @Then("^head object status code is (\\d+)$")
    public void head_object_status_code_is(int statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        TestUtil.assertEqual(headObjectOutput.getStatueCode(),statueCode);
    }

    @When("^options object with method \"([^\"]*)\" and origin \"([^\"]*)\"$")
    public void options_object_with_method_and_origin(String method, String origin) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Bucket.OptionsObjectInput input = new Bucket.OptionsObjectInput();
        input.setAccessControlRequestMethod(method);
        input.setOrigin(origin);
        optionObjectOutput = subService.optionsObject(test_object_move,input);
    }

    @Then("^options object status code is (\\d+)$")
    public void options_object_status_code_is(int statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        TestUtil.assertEqual(optionObjectOutput.getStatueCode(),statueCode);
    }

    @When("^delete object with key \"([^\"]*)\"$")
    public void delete_object(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //Bucket.DeleteObjectInput input = new Bucket.DeleteObjectInput();
        deleteObjectOutput = subService.deleteObject(objectKey);
    }

    @Then("^delete object status code is (\\d+)$")
    public void delete_object_status_code_is(int statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        TestUtil.assertEqual(deleteObjectOutput.getStatueCode(),statueCode);
    }

    @When("^delete the move object with key \"([^\"]*)\"$")
    public void delete_the_move_object(String objectKey) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //Bucket.DeleteObjectInput input = new Bucket.DeleteObjectInput();
        deleteObjectOutput2 = subService.deleteObject(objectKey);
    }

    @Then("^delete the move object status code is (\\d+)$")
    public void delete_the_move_object_status_code_is(int statueCode) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        TestUtil.assertEqual(deleteObjectOutput2.getStatueCode(),statueCode);
    }



    @When("^get object \"([^\"]*)\" with content type \"([^\"]*)\"$")
    public void get_object_with_content_type(String objectKey, String contentType) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Bucket.GetObjectInput input = new Bucket.GetObjectInput();
        input.setResponseContentType(contentType);
        getContentTypeOutput = subService.getObject(objectKey,input);
        System.out.println(getContentTypeOutput.getMessage());   
    }


    @When("^options object \"([^\"]*)\" with method \"([^\"]*)\" and origin \"([^\"]*)\"$")
    public void options_object_with_method_and_origin(String objectKey, String method, String origin) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Bucket.OptionsObjectInput input = new Bucket.OptionsObjectInput();
        input.setAccessControlRequestMethod(method);
        input.setOrigin(origin);
        optionObjectOutput = subService.optionsObject(objectKey,input);
        System.out.println(optionObjectOutput.getMessage());
    }


}

