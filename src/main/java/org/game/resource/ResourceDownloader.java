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
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.util.List;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

//http://stackoverflow.com/questions/22295337/android-downloading-file-by-updating-progress-bar
public class ResourceDownloader {
    
    private static final String CLIENT_ID = "play-with-us/v0.1";
    private static final String ACCESS_TOKEN = "BG8Lirg0MOAAAAAAAAAAC0-b5uDsvabj82WaQjgu4sTvV6LOwKrt1l2_QCixyK2D";
    
    private DbxClientV2 mClient;
    
    public ResourceDownloader() {
        mClient = new DbxClientV2(new DbxRequestConfig(CLIENT_ID), ACCESS_TOKEN);
    }
    
    public boolean hasPatchFile() {
        return false;
    }
    
    public void start() {
        
    }
    
    public interface Listener {
        
        void onPrepared(); // 몇개의 파일을 받아와야하는지 검사합니다.
        
        void onProgress();
        
        void onComplete();
    }
    
    public static void main(String args[]) throws DbxException, IOException {
        // Create Dropbox client
        DbxClientV2 client = new DbxClientV2(new DbxRequestConfig(CLIENT_ID), ACCESS_TOKEN);

        // Get current account info
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());

        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }
        
        client.files().downloadBuilder("/test.txt").download(new FileOutputStream("test.txt"));
    }
}