package github.com.stormcc.compress;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import org.anarres.lzo.LzoAlgorithm;
import org.anarres.lzo.LzoCompressor;
import org.anarres.lzo.LzoDecompressor;
import org.anarres.lzo.LzoInputStream;
import org.anarres.lzo.LzoLibrary;
import org.anarres.lzo.LzoOutputStream;
import org.xerial.snappy.Snappy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Create By: Jimmy Song
 * Create At: 2023-08-06 11:08
 */
public class ZipUtil {
    private static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    //GZip解压缩
    public static String gzipUnCompress(String inputString){
        byte[] decode = Base64.getDecoder().decode(inputString);
        return unCompress(decode, GZIP_ENCODE_UTF_8);
    }

    public static String unCompress(byte[] bytes, String encoding){
        if(bytes == null || bytes.length == 0){
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try{
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while((n = ungzip.read(buffer)) >= 0){
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        }catch (Exception e){
            throw new RuntimeException("GzipUnCompressError", e);
        }
    }

    //Gzip压缩
    public static String gzipCompress(String original){
        return Base64.getEncoder().encodeToString(compress(original, GZIP_ENCODE_UTF_8));
    }

    public static byte[] compress(String str, String encoding){
        if(str == null || str.length() == 0){
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip ;
        try{
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        }catch (Exception e){
            throw new RuntimeException("GzipCompressError", e);
        }
        return out.toByteArray();
    }

    //deflate解压缩
    public static String deflateUnCompress(String inputString){
        byte[] bytes = Base64.getDecoder().decode(inputString);
        if(bytes == null || bytes.length == 0){
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try{
            InflaterInputStream inflater = new InflaterInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while((n = inflater.read(buffer)) >= 0){
                out.write(buffer, 0, n);
            }
            return out.toString("utf-8");
        }catch (Exception e){
            throw new RuntimeException("DeflaterUnCompressError", e);
        }
    }

    //deflate压缩
    public static String deflateCompress(String original){
        if(original == null || original.length() == 0){
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DeflaterOutputStream deflater ;
        try{
            deflater = new DeflaterOutputStream(out);
            deflater.write(original.getBytes(StandardCharsets.UTF_8));
            deflater.close();
            return Base64.getEncoder().encodeToString(out.toByteArray());
        }catch (Exception e){
            throw new RuntimeException("DeflaterCompressError", e);
        }
    }

    //lzo解压缩
    public static String lzoUnCompress(String str){
        LzoDecompressor decompressor = LzoLibrary.getInstance()
                .newDecompressor(LzoAlgorithm.LZO1X, null);
        try{
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ByteArrayInputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8)));
            LzoInputStream lis = new LzoInputStream(is, decompressor);
            int count;
            byte[] buffer = new byte[256];
            while((count = lis.read(buffer)) != -1){
                os.write(buffer, 0, count);
            }
            return os.toString();
        }catch (Exception e){
            throw new RuntimeException("lzoUnCompressError", e);
        }
    }

    public static String lzoCompress(String str){
        LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(
                LzoAlgorithm.LZO1X, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        LzoOutputStream louts = new LzoOutputStream(os, compressor);
        try{
            louts.write(str.getBytes(StandardCharsets.UTF_8));
            louts.close();
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }catch (Exception e){
            throw new RuntimeException("LzoCompressError", e);
        }
    }


    //lz4解压缩
    public static String lz4UnCompress(String str){
        byte[] decode = Base64.getDecoder().decode(str.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            LZ4BlockInputStream lzis = new LZ4BlockInputStream(
                    new ByteArrayInputStream(decode));
            int count;
            byte[] buffer = new byte[2048];
            while ((count = lzis.read(buffer)) != -1) {
                baos.write(buffer, 0, count);
            }
            lzis.close();
            return baos.toString("utf-8");
        }catch (Exception e){
            throw new RuntimeException("lz4UnCompressError", e);
        }
    }

    //lz4压缩
    public static String lz4Compress(String str){
        LZ4Factory factory = LZ4Factory.fastestInstance();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        LZ4Compressor compressor = factory.fastCompressor();
        try{
            LZ4BlockOutputStream compressedOutput = new LZ4BlockOutputStream(
                    byteOutput, 2048, compressor);
            compressedOutput.write(str.getBytes(StandardCharsets.UTF_8));
            compressedOutput.close();
            return Base64.getEncoder().encodeToString(byteOutput.toByteArray());
        }catch (Exception e){
            throw new RuntimeException("lz4CompressError", e);
        }
    }


    //snappy解压缩
    public static String snappyUnCompress(String str){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            byte[] decode = Base64.getDecoder().decode(str.getBytes());
            baos.write(Snappy.uncompress(decode));
            return baos.toString();
        }catch (Exception e){
            throw new RuntimeException("snappyUnCompressError", e);
        }
    }

    //snappy压缩
    public static String snappyCompress(String str){
        try{
            byte[] compress = Snappy.compress(str);
            return Base64.getEncoder().encodeToString(compress);
        }catch (Exception e){
            throw new RuntimeException("snappyCompressError", e);
        }
    }

}
