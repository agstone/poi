package org.apache.poi.hssf.record.formula;
import org.apache.poi.util.LittleEndian;

/**
 *
 * @author Jason Height (jheight at chariot dot net dot au)
 */
public class FuncPtg extends AbstractFunctionPtg{
    
    public final static byte sid  = 0x21;
    private int numParams=0;

    private FuncPtg() {
      //Required for clone methods
    }

    /**Creates new function pointer from a byte array 
     * usually called while reading an excel file. 
     */
    public FuncPtg(byte[] data, int offset) {
        offset++;
        //field_1_num_args = data[ offset + 0 ];
        field_2_fnc_index  = LittleEndian.getShort(data,offset + 0 );
        try {
            numParams = ( (Integer)functionData[field_2_fnc_index][2]).intValue();
        } catch (NullPointerException npe) {
            numParams=0;
        }   
    }
    
     public void writeBytes(byte[] array, int offset) {
        array[offset+0]= (byte) (sid + ptgClass);
        //array[offset+1]=field_1_num_args;
        LittleEndian.putShort(array,offset+1,field_2_fnc_index);
    }
    
     public int getNumberOfOperands() {
        return numParams;
    }

    public Object clone() {
      FuncPtg ptg = new FuncPtg();
      ptg.field_1_num_args = field_1_num_args;
      ptg.field_2_fnc_index = field_2_fnc_index;
      return ptg;
    }
}