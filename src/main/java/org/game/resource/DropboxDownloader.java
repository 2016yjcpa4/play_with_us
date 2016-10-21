/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.game.resource;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;

public class DropboxDownloader {
    
    private final DbxClientV2 mClient;
    
    public DropboxDownloader(String clientId, String accessToken) {
        mClient = new DbxClientV2(new DbxRequestConfig(clientId), accessToken);
    }
    
    public FileMetadata download(String src, File dst) throws DbxException, IOException {
        return download(src, dst.getAbsolutePath());
    }
    
    public FileMetadata download(String src, String dst) throws DbxException, IOException {
        return mClient.files()
                      .downloadBuilder(src)
                      .download(new FileOutputStream(dst));
    }
}