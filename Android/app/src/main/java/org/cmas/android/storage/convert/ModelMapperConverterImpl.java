package org.cmas.android.storage.convert;

import org.modelmapper.ModelMapper;

public class ModelMapperConverterImpl<S, D> implements ModelConverter<S, D> {

    private ModelMapper modelMapper;

    public ModelMapperConverterImpl() {
        modelMapper = new ModelMapper();
    }

    @Override
    public D convertFromSource(Class<D> destClass, S sourceModel) {
        return modelMapper.map(sourceModel, destClass);
    }

    @Override
    public S convertFromDest(Class<S> sourceClass, D destModel) {
        return modelMapper.map(destModel, sourceClass);
    }
}
