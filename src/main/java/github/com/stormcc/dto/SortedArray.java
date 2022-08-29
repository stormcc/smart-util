package github.com.stormcc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Create By: Jimmy Song
 * Create At: 2022-08-29 15:24
 */
@Getter
@Setter
@NoArgsConstructor
public class SortedArray extends BaseDto implements Comparable<SortedArray> {
    private int[] array;

    public SortedArray(int[] array){
        this.array = array;
    }

    @Override
    public boolean equals(Object obj){
        if (  obj instanceof SortedArray ) {
            SortedArray other = (SortedArray) obj;
            return sortedEqual(other);
        }
        return false;
    }

    public boolean sortedEqual(SortedArray o) {
        if ( array.length != o.getArray().length ) {
            return false;
        }
        int[] one = Arrays.copyOfRange(array, 0, array.length);
        int[] two = Arrays.copyOfRange(o.getArray(), 0, o.getArray().length);
        Arrays.sort(one);
        Arrays.sort(two);
        for ( int i = 0; i < one.length; i++) {
            if ( one[i] != two[i] ){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int[] one = Arrays.copyOfRange(array, 0, array.length);
        Arrays.sort(one);
        return one[0];
    }

    @Override
    public int compareTo(@NotNull SortedArray o) {
        int c = array.length - o.getArray().length;
        if ( c != 0 ) {
            return c;
        }
        int[] one = Arrays.copyOfRange(array, 0, array.length);
        int[] two = Arrays.copyOfRange(o.getArray(), 0, o.getArray().length);
        Arrays.sort(one);
        Arrays.sort(two);
        for ( int i = 0; i < one.length; i++ ) {
            int c2 = one[i] - two[i];
            if ( c2 != 0 ) {
                return c2;
            }
        }
        return 0;
    }
}
