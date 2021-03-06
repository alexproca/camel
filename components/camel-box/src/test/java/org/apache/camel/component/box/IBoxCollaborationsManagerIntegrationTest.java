/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Camel Api Route test generated by camel-component-util-maven-plugin
 * Generated on: Tue Jun 24 22:42:08 PDT 2014
 */
package org.apache.camel.component.box;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.box.boxjavalibv2.dao.BoxCollaboration;
import com.box.boxjavalibv2.dao.BoxCollaborationRole;
import com.box.boxjavalibv2.requests.requestobjects.BoxCollabRequestObject;
import com.box.boxjavalibv2.requests.requestobjects.BoxGetAllCollabsRequestObject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.box.internal.BoxApiCollection;
import org.apache.camel.component.box.internal.IBoxCollaborationsManagerApiMethod;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for com.box.boxjavalibv2.resourcemanagers.IBoxCollaborationsManager APIs.
 */
public class IBoxCollaborationsManagerIntegrationTest extends AbstractBoxTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(IBoxCollaborationsManagerIntegrationTest.class);
    private static final String PATH_PREFIX = BoxApiCollection.getCollection().getApiName(IBoxCollaborationsManagerApiMethod.class).getName();

    private BoxCollaboration createCollaboration() throws InterruptedException {
        final Map<String, Object> headers = new HashMap<String, Object>();
        // parameter type is String
        headers.put("CamelBox.folderId", testFolderId);
        // parameter type is com.box.boxjavalibv2.requests.requestobjects.BoxCollabRequestObject
        final BoxCollabRequestObject collabObject = BoxCollabRequestObject.createCollabObject(testFolderId, null,
            "camel.test@localhost.com", BoxCollaborationRole.VIEWER);
        headers.put("CamelBox.collabRequest", collabObject);

        BoxCollaboration result = requestBodyAndHeaders("direct://CREATECOLLABORATION",
            null, headers);
        assertNotNull("createCollaboration result", result);
        // wait a moment for collaboration to register
        Thread.sleep(2000);
        return result;
    }

    private void deleteCollaboration(String collabId) throws Exception {

        final Map<String, Object> headers = new HashMap<String, Object>();
        // parameter type is String
        headers.put("CamelBox.collabId", collabId);
        // parameter type is com.box.restclientv2.requestsbase.BoxDefaultRequestObject
//        headers.put("CamelBox.defaultRequest", null);
        requestBodyAndHeaders("direct://DELETECOLLABORATION", null, headers);
    }

    @Test
    public void testGetAllCollaborations() throws Exception {
        // using com.box.boxjavalibv2.requests.requestobjects.BoxGetAllCollabsRequestObject message body for single parameter "getAllCollabsRequest"
        final BoxGetAllCollabsRequestObject collabRequest =
            BoxGetAllCollabsRequestObject.getAllCollaborationsRequestObject(BoxCollaboration.STATUS_PENDING);

        List result = requestBody("direct://GETALLCOLLABORATIONS", collabRequest);
        assertNotNull("getAllCollaborations: " + result);
        LOG.debug("getAllCollaborations: " + result);
    }

    @Test
    public void testGetCollaboration() throws Exception {
        final BoxCollaboration collaboration = createCollaboration();

        try {
            final Map<String, Object> headers = new HashMap<String, Object>();
            // parameter type is String
            headers.put("CamelBox.collabId", collaboration.getId());
            // parameter type is com.box.restclientv2.requestsbase.BoxDefaultRequestObject
            headers.put("CamelBox.defaultRequest", null);
            
            BoxCollaboration result = requestBodyAndHeaders("direct://GETCOLLABORATION", null, headers);
            assertNotNull("getCollaboration result", result);
            LOG.debug("getCollaboration: " + result);
        } finally {
            deleteCollaboration(collaboration.getId());
        }
    }

    @Ignore("BoxClient SDK has a bug in UpdateCollaborationRequest.java, the URI constant should be collaborations")
    @Test
    public void testUpdateCollaboration() throws Exception {
        final BoxCollaboration collaboration = createCollaboration();

        try {
            final Map<String, Object> headers = new HashMap<String, Object>();
            // parameter type is String
            headers.put("CamelBox.collabId", collaboration.getId());
            // parameter type is com.box.boxjavalibv2.requests.requestobjects.BoxCollabRequestObject
            final BoxCollabRequestObject requestObject = BoxCollabRequestObject.updateCollabObjects(
                BoxCollaborationRole.EDITOR);
            headers.put("CamelBox.collabRequest", requestObject);

            BoxCollaboration result = requestBodyAndHeaders("direct://UPDATECOLLABORATION", null, headers);
            assertNotNull("updateCollaboration result", result);
            LOG.debug("updateCollaboration: " + result);
        } finally {
            deleteCollaboration(collaboration.getId());
        }
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                // test route for createCollaboration
                from("direct://CREATECOLLABORATION")
                  .to("box://" + PATH_PREFIX + "/createCollaboration");

                // test route for deleteCollaboration
                from("direct://DELETECOLLABORATION")
                  .to("box://" + PATH_PREFIX + "/deleteCollaboration");

                // test route for getAllCollaborations
                from("direct://GETALLCOLLABORATIONS")
                  .to("box://" + PATH_PREFIX + "/getAllCollaborations?inBody=getAllCollabsRequest");

                // test route for getCollaboration
                from("direct://GETCOLLABORATION")
                  .to("box://" + PATH_PREFIX + "/getCollaboration");

                // test route for updateCollaboration
                from("direct://UPDATECOLLABORATION")
                  .to("box://" + PATH_PREFIX + "/updateCollaboration");

            }
        };
    }
}
