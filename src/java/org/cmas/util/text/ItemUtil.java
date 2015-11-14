package org.cmas.util.text;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemUtil {

   public static final String ITEM_IDS_DELIMITER = ",";

    private ItemUtil() {
    }

    public static List<Long> extractItemIds(@Nullable String ids){
        if(StringUtil.isTrimmedEmpty(ids)){
            return Collections.emptyList();
        }
        else{
            @SuppressWarnings({"ConstantConditions"})
            String[] itemIdsArr = ids.split(ITEM_IDS_DELIMITER);
            List<Long> result = new ArrayList<Long>(itemIdsArr.length);
            for(String itemIdStr : itemIdsArr){
                try{
                    long itemId = Long.parseLong(itemIdStr);
                    result.add(itemId);
                }
                catch (NumberFormatException ignored){

                }
            }
            return result;
        }
    }
}
