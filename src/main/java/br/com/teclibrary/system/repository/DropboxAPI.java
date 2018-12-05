package br.com.teclibrary.system.repository;

import br.com.teclibrary.system.db.SystemParams;
import br.com.teclibrary.system.impls.ModelOptional;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.users.FullAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class DropboxAPI extends DbxClientV2 {

    private static final Logger logger = LoggerFactory.getLogger(DropboxAPI.class);
    private static final DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/TecLibrary").build();
    private static final RepositoryCache REPOSITORY_CACHE = new RepositoryCache();
    private static String TOKEN = "undefined";

    static {
        try {
            TOKEN = SystemParams.getParametro("teclibrary-app.dropbox-api.acesskey");
            DbxClientV2 dbxClientV2 = new DbxClientV2(config, TOKEN);
            createDefaultDiretorios(dbxClientV2, "/imgs");
            createDefaultDiretorios(dbxClientV2, "/pdfs");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public DropboxAPI() {
        super(config, TOKEN);
    }

    private static final void createDefaultDiretorios(DbxClientV2 dbxClientV2, String folder) throws Exception {
        logger.warn("Verificando se diretório existe: " + folder);
        ModelOptional<ListFolderResult> optional = new ModelOptional<>();
        optional.set(dbxClientV2.files().listFolder(folder));
        if (!optional.contains()) {
            logger.warn("Criando diretório " + folder + "em DropboxAPI.");
            dbxClientV2.files().createFolderV2(folder);
            logger.info("Criado com sucesso!");
        }
    }

    public static RepositoryCache getRepositoryCache() {
        return REPOSITORY_CACHE;
    }

    public FullAccount getFullAccount() throws DbxException {
        return this.users().getCurrentAccount();
    }

    public void uploadFile(String fullPath, InputStream file, Integer ID, FileType fileType) throws DbxException, IOException {
        ModelOptional<ListFolderResult> folder = new ModelOptional<>();
        folder.set(this.files().listFolder(fullPath));
        logger.warn("Checando se o diretório existe: ".concat(fullPath));
        if (!folder.contains())
            throw new DbxException("Não foi possível estabelecer comunicação com o diretório: ".concat(fullPath));
        logger.warn("Diretório validado com sucesso");
        String fullFilePath = String.format("%s/%s%s", fullPath, ID, fileType.toString());
        logger.warn("Checando se arquivo já existe: ".concat(fullFilePath));
        if (fileExists(fullPath, ID, fileType)) {
            logger.warn("Arquivo já existia, removendo o mesmo para substituição");
            removeFile(fullFilePath);
            getRepositoryCache().deleteFromCache(fullFilePath);
        }
        logger.warn("Iniciando gravação do novo arquivo");
        FileMetadata fileMetadata = this.files().upload(fullFilePath).uploadAndFinish(file);
    }

    public void removeFile(String fullPath) throws DbxException {
        this.files().deleteV2(fullPath);
        getRepositoryCache().deleteFromCache(fullPath);
        logger.warn("Arquivo excluído com sucesso: ".concat(fullPath));
    }

    public ModelFile downloadFile(String fullPath) throws DbxException {
        if (getRepositoryCache().containsFile(fullPath))
            return getRepositoryCache().getFileFromCache(fullPath);
        InputStream inputStream = this.files().download(fullPath).getInputStream();
        ModelFile fileCache = new ModelFile(fullPath, inputStream);
        getRepositoryCache().addToCache(fileCache);
        return fileCache;
    }

    public Boolean fileExists(String fullPath, Integer ID, FileType fileType) {
        try {
            return this.files().listFolder(fullPath).getEntries().stream()
                    .filter(entry -> entry.getName().equals(String.valueOf(ID).concat(fileType.toString())))
                    .count() > 0;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    public void clearCache(String ID) {
        if (ID == null || ID.isEmpty()) {
            getRepositoryCache().clearAllCache();
        } else {
            getRepositoryCache().deleteFromCache(ID);
        }
    }
}