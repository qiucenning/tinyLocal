/**
 *  Copyright (c) 1997-2013, tinygroup.org (luo_guo@live.cn).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * --------------------------------------------------------------------------
 *  版权 (c) 1997-2013, tinygroup.org (luo_guo@live.cn).
 *
 *  本开源软件遵循 GPL 3.0 协议;
 *  如果您不遵循此协议，则不被允许使用此文件。
 *  你可以从下面的地址获取完整的协议文本
 *
 *       http://www.gnu.org/licenses/gpl.html
 */
package org.tinygroup.net.test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class KryoTest {
	public static void main(String[] args) throws Exception {
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

		Output output = new Output(1024000);
		long s = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			OutputStream fileOutputStream = new ByteArrayOutputStream();
			output.setOutputStream(fileOutputStream);
			kryo.writeClassAndObject(output, Simple.getSimple());
			output.flush();
			fileOutputStream.close();
		}
		long e = System.currentTimeMillis();
		System.out.println(e - s);
		ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
		output.setOutputStream(fileOutputStream);
		kryo.writeClassAndObject(output, Simple.getSimple());
		output.flush();
		fileOutputStream.close();
		s = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {

			Input input = new Input(new ByteArrayInputStream(
					fileOutputStream.toByteArray()));
			Object o = kryo.readClassAndObject(input);
		}
		e = System.currentTimeMillis();
		System.out.println(e - s);
	}
}
