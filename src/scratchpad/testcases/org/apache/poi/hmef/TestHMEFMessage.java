/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hmef;

import junit.framework.TestCase;

import org.apache.poi.POIDataSamples;

public final class TestHMEFMessage extends TestCase {
    private static final POIDataSamples _samples = POIDataSamples.getHMEFInstance();

	public void testOpen() throws Exception {
		HMEFMessage msg = new HMEFMessage(
                _samples.openResourceAsStream("quick-winmail.dat")
		);

		assertNotNull(msg);
	}

	public void testCounts() throws Exception {
      HMEFMessage msg = new HMEFMessage(
            _samples.openResourceAsStream("quick-winmail.dat")
      );
      
      // Should have 4 attributes on the message
      assertEquals(4, msg.getMessageAttributes().size());
      
      // And should have 54 MAPI attributes on it
      assertEquals(54, msg.getMessageMAPIAttributes().size());
      
      
      // Should have 5 attachments
      assertEquals(5, msg.getAttachments().size());
      
      // Each attachment should have 6 normal attributes, and 
      //  20 or so MAPI ones
      for(Attachment attach : msg.getAttachments()) {
         int attrCount = attach.getAttributes().size();
         int mapiAttrCount = attach.getMAPIAttributes().size();
         
         assertEquals(6, attrCount);
         // TODO
//         assertTrue("Should be 3-4 attributes, found " + mapiAttrCount, mapiAttrCount >= 20);
//         assertTrue("Should be 3-4 attributes, found " + mapiAttrCount, mapiAttrCount <= 25);
      }
      
      
      // TODO
	}
	
	public void testBasicMessageAttributes() throws Exception {
      HMEFMessage msg = new HMEFMessage(
            _samples.openResourceAsStream("quick-winmail.dat")
      );
      
      // Should have version, codepage, class and MAPI
      assertEquals(4, msg.getMessageAttributes().size());
      assertNotNull(msg.getMessageAttribute(Attribute.ID_TNEFVERSION));
      assertNotNull(msg.getMessageAttribute(Attribute.ID_OEMCODEPAGE));
      assertNotNull(msg.getMessageAttribute(Attribute.ID_MESSAGECLASS));
      assertNotNull(msg.getMessageAttribute(Attribute.ID_MAPIPROPERTIES));
      
      // Check the order
      assertEquals(Attribute.ID_TNEFVERSION, msg.getMessageAttributes().get(0).getId());
      assertEquals(Attribute.ID_OEMCODEPAGE, msg.getMessageAttributes().get(1).getId());
      assertEquals(Attribute.ID_MESSAGECLASS, msg.getMessageAttributes().get(2).getId());
      assertEquals(Attribute.ID_MAPIPROPERTIES, msg.getMessageAttributes().get(3).getId());
      
      // Check some that aren't there
      assertNull(msg.getMessageAttribute(Attribute.ID_AIDOWNER));
      assertNull(msg.getMessageAttribute(Attribute.ID_ATTACHDATA));
      
      // Now check the details of one or two
      // TODO
	}
   
   public void testBasicMessageMAPIAttributes() throws Exception {
      // TODO
   }
   
   public void testBasicAttachments() throws Exception {
      // TODO
   }
   
   public void testMessageAttributeDetails() throws Exception {
      // TODO
   }
}