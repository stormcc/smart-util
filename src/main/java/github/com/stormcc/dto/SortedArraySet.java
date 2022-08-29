package github.com.stormcc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Create By: Jimmy Song
 * Create At: 2022-08-29 15:43
 */
@Getter
@Setter
public class SortedArraySet  extends BaseDto {
    Set<SortedArray> set;

    public SortedArraySet(){
        this.set = new HashSet<>();
    }

    public void add(SortedArray sortedArray){
        set.add(sortedArray);
    }
}
