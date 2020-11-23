package org.cmas.android.storage.convert;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

public class ModelMapperConverterImpl<S, D> implements ModelConverter<S, D> {

    private ModelMapper modelMapper;

    public ModelMapperConverterImpl() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                   .setFieldMatchingEnabled(true)
                   .setFieldAccessLevel(Configuration.AccessLevel.PUBLIC);
    }

    @Override
    public D convertFromSource(Class<D> destClass, S sourceModel) {
        return modelMapper.map(sourceModel, destClass);
    }

    @Override
    public S convertFromDest(Class<S> sourceClass, D destModel) {
        return modelMapper.map(destModel, sourceClass);
    }

//    public static void main(String[] args) {
//        Country country = new Country();
//        country.setId(1);
//        country.setCode("AFG");
//        country.setIso3166_1_alpha_2_code("ad");
//        country.setDeleted(true);
//        country.setName("asdasd");
//        country.setVersion(23);
//
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration()
//                   .setFieldMatchingEnabled(true)
//                   .setFieldAccessLevel(Configuration.AccessLevel.PUBLIC);
//
//        org.cmas.android.storage.entities.Country countryMapped = modelMapper.map(country,
//                                                                        org.cmas.android.storage.entities.Country.class);
//
//        System.out.println(countryMapped.id);
//        System.out.println(countryMapped.name);
//        System.out.println(countryMapped.code);
//        System.out.println(countryMapped.iso3166_1_alpha_2_code);
//        System.out.println(countryMapped.version);
//        System.out.println(countryMapped.deleted);
//    }
}
