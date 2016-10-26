package com.github.yjcpaj4.play_with_us.resource;

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