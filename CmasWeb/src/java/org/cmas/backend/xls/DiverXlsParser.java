package org.cmas.backend.xls;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.service.user.ProgressListener;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

/**
 * Created on Apr 26, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiverXlsParser {

    Collection<Diver> getDivers(InputStream file, ProgressListener progressListener) throws Exception;

    Collection<Diver> getDivers(File file, ProgressListener progressListener) throws Exception;
}
