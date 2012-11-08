/**
 *  Copyright 2012 LiveRamp
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.liveramp.megadesk.resource.lib;

import com.liveramp.megadesk.Megadesk;
import com.liveramp.megadesk.data.lib.EqualityDataCheck;
import com.liveramp.megadesk.data.lib.NotEmptyStringDataCheck;
import com.liveramp.megadesk.resource.BaseResource;
import com.liveramp.megadesk.resource.Read;
import com.liveramp.megadesk.resource.Resource;
import com.liveramp.megadesk.serialization.lib.StringSerialization;

public class StringResource extends BaseResource<String> implements Resource<String> {

  public StringResource(Megadesk megadesk, String id) throws Exception {
    super(id, megadesk.getResourceDriver(id), new StringSerialization());
  }

  public Read equals(String data) {
    return new Read<String>(this, new EqualityDataCheck<String>(data));
  }

  public Read notEmpty() {
    return new Read<String>(this, new NotEmptyStringDataCheck());
  }
}