package br.com.teclibrary.system.repository;

import java.util.ArrayList;
import java.util.List;

public class RepositoryCache {

    private List<ModelFile> fileCacheList = new ArrayList<>();

    public Boolean containsFile(String ID) {
        return fileCacheList.stream().filter(files -> files.getFileID().equals(ID)).count() > 0;
    }

    public ModelFile getFileFromCache(String ID) {
        return fileCacheList.stream().filter(files -> files.getFileID().equals(ID)).findFirst().get();
    }

    public void clearAllCache() {
        this.fileCacheList.clear();
    }

    public void addToCache(ModelFile modelFileCache) {
        if (containsFile(modelFileCache.getFileID()))
            deleteFromCache(modelFileCache.getFileID());
        this.fileCacheList.add(modelFileCache);
    }

    public void deleteFromCache(String ID) {
        if (containsFile(ID))
            this.fileCacheList.remove(this.fileCacheList.stream().filter(cache -> cache.getFileID().equals(ID)).findFirst().get());
    }
}
