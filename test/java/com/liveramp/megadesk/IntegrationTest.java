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

package com.liveramp.megadesk;

import com.google.common.base.Throwables;
import com.liveramp.megadesk.curator.CuratorResource;
import com.liveramp.megadesk.curator.CuratorStep;
import com.liveramp.megadesk.resource.Read;
import com.liveramp.megadesk.resource.Reads;
import com.liveramp.megadesk.resource.Resource;
import com.liveramp.megadesk.resource.Writes;
import com.liveramp.megadesk.step.Step;
import com.liveramp.megadesk.test.BaseTestCase;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryNTimes;
import com.netflix.curator.test.TestingServer;

public class IntegrationTest extends BaseTestCase {

  public void testWorkflow() throws Exception {

    TestingServer testingServer = new TestingServer(12000);
    final CuratorFramework curator;
    curator = CuratorFrameworkFactory.builder()
        .connectionTimeoutMs(1000)
        .retryPolicy(new RetryNTimes(10, 500))
        .connectString(testingServer.getConnectString())
        .build();
    curator.start();

    final int[] resultA = {-1};
    final int[] resultB = {-1};

    Thread stepA = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Resource resourceA = new CuratorResource(curator, "resourceA");
          Resource resourceB = new CuratorResource(curator, "resourceB");
          Resource resourceC = new CuratorResource(curator, "resourceC");
          Step step = new CuratorStep(curator,
              "stepA",
              Reads.list(new Read(resourceA, "ready"), new Read(resourceB, "ready")),
              Writes.list(resourceC));
          step.attempt();
          //... do things
          resultA[0] = 0;
          step.complete();
        } catch (Exception e) {
          throw Throwables.propagate(e);
        }
      }
    }, "stepA");

    Thread stepB = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Resource resourceC = new CuratorResource(curator, "resourceC");
          Resource resourceD = new CuratorResource(curator, "resourceD");
          Step step = new CuratorStep(curator,
              "stepB",
              Reads.list(new Read(resourceC, "ready")),
              Writes.list(resourceD));
          step.attempt();
          //... do things
          resultB[0] = 1;
          step.complete();
        } catch (Exception e) {
          throw Throwables.propagate(e);
        }
      }
    }, "stepB");

    stepA.start();
    stepB.start();

    stepA.join();
    stepB.join();

    assertEquals(0, resultA[0]);
    assertEquals(1, resultB[0]);
  }
}
