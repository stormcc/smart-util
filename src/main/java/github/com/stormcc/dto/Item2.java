package github.com.stormcc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Create By: Jimmy Song
 * Create At: 2022-09-06 17:36
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item2<I1, I2> extends BaseDto{
    private I1 i0;
    private I2 i1;
}
