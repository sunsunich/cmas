package org.cmas.presentation.dao.user;

import org.cmas.entities.DeviceType;
import org.cmas.entities.User;
import org.cmas.presentation.entities.user.Device;
import org.cmas.util.dao.HibernateDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 22/11/12
 * Time: 05:10
 */
public interface DeviceDao extends HibernateDao<Device> {

    Device getByClientDeviceId(String clientDeviceId);

    List<String> getPushRegIdByUserAndDeviceType(User user, DeviceType deviceType);
}
