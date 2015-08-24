/*
 * Copyright 2015 Rackspace
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */



package com.rackspacecloud.blueflood.io.serializers;

import com.rackspacecloud.blueflood.io.Constants;
import com.rackspacecloud.blueflood.types.BluefloodEnumRollup;
import junit.framework.Assert;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;

public class EnumRollupSerializationTest {

    @Test
    public void testEnumV1RoundTrip() throws IOException {
        BluefloodEnumRollup e0 = new BluefloodEnumRollup().withEnumValue("enumValue1", (long) 1).withEnumValue("enumValuu2", 5L);
        BluefloodEnumRollup e1 = new BluefloodEnumRollup().withEnumValue("t4.enum.abcdefg.hijklmnop.qrstuvw.xyz.ABCDEFG.HIJKLMNOP.QRSTUVW.XYZ.abcdefg.hijklmnop.qrstuvw.xyz.met", 34454722343L)
                            .withEnumValue("t5.enum.abcdefg.hijklmnop.qrstuvw.xyz.ABCDEFG.HIJKLMNOP.QRSTUVW.XYZ.abcdefg.hijklmnop.qrstuvw.xyz.met", 34454722343L);

        if (System.getProperty("GENERATE_ENUM_SERIALIZATION") != null) {
            OutputStream os = new FileOutputStream("src/test/resources/serializations/enum_version_" + Constants.VERSION_1_ENUM_ROLLUP + ".bin", false);
            os.write(Base64.encodeBase64(new NumericSerializer.EnumRollupSerializer().toByteBuffer(e0).array()));
            os.write("\n".getBytes());
            os.write(Base64.encodeBase64(new NumericSerializer.EnumRollupSerializer().toByteBuffer(e1).array()));
            os.write("\n".getBytes());
            os.close();
        }

        Assert.assertTrue(new File("src/test/resources/serializations").exists());

        int count = 0;
        int version = 0;
        final int maxVersion = Constants.VERSION_1_ENUM_ROLLUP;
        while (version <= maxVersion) {
            BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/serializations/enum_version_" + version + ".bin"));

            ByteBuffer bb = ByteBuffer.wrap(Base64.decodeBase64(reader.readLine().getBytes()));
            BluefloodEnumRollup ee0 = NumericSerializer.serializerFor(BluefloodEnumRollup.class).fromByteBuffer(bb);
            Assert.assertEquals(e0, ee0);

            bb = ByteBuffer.wrap(Base64.decodeBase64(reader.readLine().getBytes()));
            BluefloodEnumRollup ee1 = NumericSerializer.serializerFor(BluefloodEnumRollup.class).fromByteBuffer(bb);
            Assert.assertEquals(e1, ee1);

            Assert.assertFalse(ee0.equals(ee1));
            version++;
            count++;
        }

        Assert.assertTrue(count > 0);
    }
}
