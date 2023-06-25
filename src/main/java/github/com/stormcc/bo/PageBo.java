package github.com.stormcc.bo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Create By: Jimmy Song
 * Create At: 2023-06-21 17:40
 */
@Getter
@Setter
@NoArgsConstructor
public class PageBo extends BaseBo {
    /*
    从0开始
     */
    private Long offset;
    /*
    每页多少行
     */
    private Integer batchNumber;
}
