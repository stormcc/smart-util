package github.com.stormcc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RangeDto extends BaseDto{
    private Integer start;
    private Integer end;
}
