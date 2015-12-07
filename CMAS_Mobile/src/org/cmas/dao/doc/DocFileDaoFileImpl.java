package org.cmas.dao.doc;

import android.content.Context;
import org.cmas.entities.doc.DocFile;
import org.cmas.entities.doc.Document;
import org.cmas.util.Base64Coder;
import org.cmas.util.StringUtil;
import org.cmas.util.android.DeviceInfo;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class DocFileDaoFileImpl implements DocFileDao {

    public static String getFileStoreLocation(Context context) {
        if (DeviceInfo.isExternalStorageWritable()) {
            return context.getExternalFilesDir(null).getAbsolutePath()
                    ;
        } else {
            return context.getFilesDir().getAbsolutePath();
        }
    }

    public static File[] listFilesMatching(File root, String regex) {
        if (!root.isDirectory()) {
            throw new IllegalArgumentException(root + " is no directory.");
        }
        final Pattern p = Pattern.compile(regex); // careful: could also throw an exception!
        return root.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return p.matcher(file.getName()).matches();
            }
        });
    }

    private String generateFileName(Context context, Document document, DocFile docFile) {
        String fileStoreLocation = getFileStoreLocation(context);
        File fileStore = new File(fileStoreLocation);
        String pattern = Pattern.quote(
                getFileBeginning(document)
        )
                + "_\\d+\\."
                + docFile.getExt();
        int maxDocFileNumber = listFilesMatching(fileStore, pattern).length;

        int nextDocFileNUmber = maxDocFileNumber + 1;
        return getFileBeginning(document)
                + '_' + nextDocFileNUmber
                + '.' + docFile.getExt();
    }

    private String getFileBeginning(Document document) {
        long id = document.getId();
        String name = document.getName();
        return (id == 0L ? "tmp" : id) + "_" + (StringUtil.isTrimmedEmpty(name) ? "tmp" : name);
    }

    private String getFileBeginningForDeletion(Document document) {
        long id = document.getId();
        return (id == 0L ? "tmp" : id) + "_";
    }


    @Override
    public String save(Context context, Document document, DocFile docFile) throws Exception {
        String fileName = generateFileName(context, document, docFile);
        String fullPath = getFileStoreLocation(context) + File.separator + fileName;
        File file = new File(fullPath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("cannot save file for document " + getFileBeginning(document)
                );
            }
        }
        if (!file.createNewFile()) {
            throw new RuntimeException("cannot save file for document id=" + getFileBeginning(document)
            );
        }
        String fileData = "";//docFile.getFile(); old protocol
        if (!StringUtil.isTrimmedEmpty(fileData)) {
            FileOutputStream outputStream = new FileOutputStream(file);
            try {
                outputStream.write(
                        Base64Coder.decode(fileData)
                );
                outputStream.close();
            } finally {
                outputStream.close();
            }
        }

        return fileName;
    }

    @Override
    public List<File> getFiles(Context context, Document document) {
        String fileStoreLocation = getFileStoreLocation(context);
        File fileStore = new File(fileStoreLocation);
        String pattern = Pattern.quote(getFileBeginningForDeletion(document))
                + ".+"
                + "_\\d+\\."
                + ".+";
        return Arrays.asList(listFilesMatching(fileStore, pattern));
    }

    @Override
    public void delete(Context context, String fileName) {
        String fullPath = getFileStoreLocation(context) + File.separator + fileName;
        File file = new File(fullPath);
        file.delete();
    }

    @Override
    public void deleteAllForDocument(Context context, Document document) {
        List<File> files = getFiles(context, document);
        for (File file : files) {
            delete(context, file.getName());
        }
    }
}
