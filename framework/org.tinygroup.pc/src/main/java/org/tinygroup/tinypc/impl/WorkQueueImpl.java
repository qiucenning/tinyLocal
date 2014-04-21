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
package org.tinygroup.tinypc.impl;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinypc.ObjectStorage;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.WorkQueue;
import org.tinygroup.tinypc.WorkStatus;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 14-1-23.
 */
public class WorkQueueImpl implements WorkQueue {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6270528078815279562L;
	List<Work> workList = new ArrayList<Work>();
    ObjectStorage objectStorage = null;
    private static Logger logger = LoggerFactory.getLogger(WorkQueueImpl.class);

    public WorkQueueImpl() throws RemoteException {

    }

    public ObjectStorage getObjectStorage() {
        return objectStorage;
    }

    public void setObjectStorage(ObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
        try {
            List<Work> workList = objectStorage.loadObjects("Work");
            this.workList.addAll(workList);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public synchronized void add(Work work) throws RemoteException {
        if (work.isNeedSerialize()) {
            if (objectStorage == null) {
                throw new RuntimeException("没有对象仓库对象实例存在！");
            }
            try {
                objectStorage.saveObject(work, "Work");
            } catch (IOException e) {
                logger.error(e);
                throw new RuntimeException(String.format("序列化Work:%s %s时出现异常", work.getType()), e);
            }
        }
        workList.add(work);
    }

    public synchronized void updateWorkStatus(Work work, WorkStatus workStatus) throws RemoteException {
        for (Work w : workList) {
            if (w.getId() == work.getId()) {
                w.setWorkStatus(workStatus);
            }
        }
    }

    public synchronized WorkStatus getWorkStatus(Work work) throws RemoteException {
        for (Work w : workList) {
            if (w.getId() == work.getId()) {
                return work.getWorkStatus();
            }
        }
        throw new RuntimeException(String.format("找不到查询的工作:%s-%s！", work.getType(), work.getId()));
    }

    public synchronized void remove(Work work) throws RemoteException {
        workList.remove(work);
    }

    public synchronized int size() throws RemoteException {
        return workList.size();
    }

    public synchronized List<Work> getWorkList() throws RemoteException {
        return workList;
    }

    public synchronized Work getIdleWork() throws RemoteException {
        for (Work work : workList) {
            if (work.getWorkStatus().equals(WorkStatus.WAITING)) {
                return work;
            }
        }
        return null;
    }

    public synchronized List<Work> getWorkList(String type, WorkStatus workStatus) {
        List<Work> list = new ArrayList<Work>();
        for (Work work : workList) {
            if (work.getType().equals(type) && work.getWorkStatus().equals(workStatus)) {
                list.add(work);
            }
        }
        return list;
    }

    public synchronized List<Work> getWorkList(WorkStatus workStatus) {
        List<Work> list = new ArrayList<Work>();
        for (Work work : workList) {
            if (work.getWorkStatus().equals(workStatus)) {
                list.add(work);
            }
        }
        return list;
    }

    public synchronized void replace(Work oldWork, Work newWork) throws RemoteException {
        remove(oldWork);
        add(newWork);
    }

    public void moveToLast(Work work) {
        workList.remove(work);
        workList.add(work);
    }
}
