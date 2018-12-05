package br.com.teclibrary.system.repository;

import br.com.teclibrary.entity.Livro;
import br.com.teclibrary.system.task.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class RepositoryCtrl {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryCtrl.class);
    private static final DropboxAPI DROPBOX_API = new DropboxAPI();

    public static ModelFile retrieveArquivo(String folder, String ID, FileType FILE_TYPE) {
        try {
            return getDropboxApi().downloadFile(folder.concat("/").concat(ID).concat(FILE_TYPE.toString()));
        } catch (Exception ex) {
            logger.error(String.format("Folder: %s | ID: %s | FileType: %s", folder, ID, FILE_TYPE.toString()));
            ex.printStackTrace();
            return null;
        }
    }

    public static void mergeFileToDatabase(String folder, MultipartFile file, Livro livro, FileType FYLE_TYPE) {
        if (FYLE_TYPE == FileType.Image) {
            try {
                getDropboxApi().uploadFile(folder, file.getInputStream(), livro.getCodigo(), FYLE_TYPE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            new AsyncTask(() -> {
                try {
                    getDropboxApi().uploadFile(folder, file.getInputStream(), livro.getCodigo(), FYLE_TYPE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).trigger();
        }
    }

    public static DropboxAPI getDropboxApi() {
        return DROPBOX_API;
    }

}
