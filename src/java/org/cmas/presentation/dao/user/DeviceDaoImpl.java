package org.cmas.presentation.dao.user;

import org.cmas.entities.DeviceType;
import org.cmas.entities.User;
import org.cmas.presentation.entities.user.Device;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 22/11/12
 * Time: 05:11
 */
public class DeviceDaoImpl extends HibernateDaoImpl<Device> implements DeviceDao{

    @Override
    public Device getByClientDeviceId(String clientDeviceId) {
        Criteria crit = createCriteria();
        return (Device) crit.add(Restrictions.eq("clientDeviceId", clientDeviceId))
                            .setCacheable(true).uniqueResult();
    }

    @Override
    public List<String> getPushRegIdByUserAndDeviceType(User user, DeviceType deviceType) {
        Criteria crit = createCriteria();
        //todo modify
        //noinspection unchecked
        return (List<String>) crit.add(Restrictions.eq("deviceType", deviceType))
                                  .add(Restrictions.eq("user", user))
                                  .setProjection(Projections.groupProperty("pushServiceRegId"))
                            .setCacheable(true).list();
    }
}
