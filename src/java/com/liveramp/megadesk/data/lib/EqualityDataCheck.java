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

package com.liveramp.megadesk.data.lib;

import com.liveramp.megadesk.data.DataCheck;
import com.liveramp.megadesk.resource.Resource;

public class EqualityDataCheck<T> implements DataCheck<T> {

  private final T data;

  public EqualityDataCheck(T data) {
    this.data = data;
  }

  @Override
  public boolean check(Resource<T> resource) throws Exception {
    T currentData = resource.read();
    return (data == null && currentData == null)
        || (data != null && data.equals(currentData));
  }

  @Override
  public String toString() {
    return "[EqualityDataCheck: '" + data + "']";
  }
}
