/*
 * Copyright [2013-2021], Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.polardbx.common;

import com.alibaba.polardbx.common.utils.GeneralUtil;
import com.alibaba.polardbx.common.utils.TStringUtil;

public enum Engine {

    INNODB,
    MRG_MYISAM,
    BLACKHOLE,
    MYISAM,
    CSV,
    ARCHIVE,
    PERFORMANCE_SCHEMA,
    FEDERATED,
    LOCAL_DISK,
    EXTERNAL_DISK,
    S3,
    ABS,
    OSS,
    NFS,
    MEMORY,
    COLUMNAR;

    public static final Engine DEFAULT_COLUMNAR_ENGINE = OSS;

    public static Engine of(String engineName) {
        if (TStringUtil.isEmpty(engineName)) {
            return INNODB;
        }
        try {
            return Engine.valueOf(engineName.toUpperCase());
        } catch (Throwable t) {
            throw GeneralUtil.nestedException("Unknown engine name:" + engineName);
        }
    }

    public static boolean hasCache(Engine engine) {
        if (engine == null) {
            return false;
        }
        switch (engine) {
        case OSS:
        case EXTERNAL_DISK:
        case NFS:
        case S3:
        case ABS:
            return true;
        default:
            return false;
        }
    }

    public static boolean isFileStore(Engine engine) {
        if (engine == null) {
            return false;
        }
        switch (engine) {
        case OSS:
        case LOCAL_DISK:
        case EXTERNAL_DISK:
        case S3:
        case NFS:
        case ABS:
            return true;
        default:
            return false;
        }
    }

    public static boolean supportColumnar(Engine engine) {
        if (engine == null) {
            return false;
        }
        switch (engine) {
        case OSS:
        case LOCAL_DISK:
        case EXTERNAL_DISK:
        case NFS:
        case S3:
        case ABS:
        case COLUMNAR:
            return true;
        default:
            return false;
        }
    }

    public static boolean isFileStore(String engineName) {
        return isFileStore(of(engineName));
    }
}
