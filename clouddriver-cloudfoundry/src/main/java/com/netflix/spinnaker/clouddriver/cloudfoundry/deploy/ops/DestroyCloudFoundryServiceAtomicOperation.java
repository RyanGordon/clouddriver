/*
 * Copyright 2018 Pivotal, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.ops;

import com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.description.DestroyCloudFoundryServiceDescription;
import com.netflix.spinnaker.clouddriver.data.task.Task;
import com.netflix.spinnaker.clouddriver.data.task.TaskRepository;
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DestroyCloudFoundryServiceAtomicOperation implements AtomicOperation<Void> {
  private static final String PHASE = "DELETE_SERVICE";
  private final DestroyCloudFoundryServiceDescription description;

  private static Task getTask() {
    return TaskRepository.threadLocalTask.get();
  }

  @Override
  public Void operate(List priorOutputs) {
    Task task = getTask();
    task.updateStatus(PHASE, "Initializing the removal of service instance '" + description.getServiceName() + "' from space " + description.getSpace().getName());
    description.getClient()
      .getServiceInstances()
      .destroyServiceInstance(description.getSpace(), description.getServiceName(), description.getTimeout());
    task.updateStatus(PHASE, "Done removing service instance '" + description.getServiceName() + "'");
    return null;
  }
}
