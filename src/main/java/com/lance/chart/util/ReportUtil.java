package com.lance.chart.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ReportUtil.class);
    public static final String YYYYMMDDHHMMSS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static final SimpleDateFormat sdf1 = new SimpleDateFormat(YYYYMMDDHHMMSS_SSS, Locale.US);

    private ReportUtil() {
    }

    public static String toDateStr(Date date) {
        if (null == date) {
            return null;
        }

        return sdf1.format(date);
    }

    public static String getFormatedDate(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(date);
    }

    public static Date toDate(String dateStr) throws ParseException {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }

        return sdf1.parse(dateStr);
    }

    public static double round(double val, int scale) {
        return new BigDecimal(String.valueOf(val)).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static void removeIfExist(String filePath) {
        File file = new File(filePath);
        removeDirIfExist(file);
    }

    private static void removeDirIfExist(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                String[] childrens = file.list();
                for (String child : childrens) {
                    removeDirIfExist(new File(file, child));
                }
                boolean deleted = file.delete();
                if (!deleted) {
                    LOG.warn("Delete Directory {} failed.", file.getAbsolutePath());
                }
            } else {
                boolean deleted = file.delete();
                if (!deleted) {
                    LOG.warn("Delete File {} failed.", file.getAbsolutePath());
                }
            }
        }
    }

    public static void createDirIfNotExist(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String getFormatedNumber(Double val, String format) {
        return new DecimalFormat(format).format(val);
    }

    public static void zip(String sourceFolder, String zipFilePath) {
        OutputStream os = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;
        try {
            os = new FileOutputStream(zipFilePath);
            bos = new BufferedOutputStream(os);
            zos = new ZipOutputStream(bos);
            File file = new File(sourceFolder);
            String basePath = null;
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(os);
        }
    }

    private static void zipFile(File parentFile, String basePath, ZipOutputStream zos) throws Exception {
        File[] files = new File[0];
        if (parentFile.isDirectory()) {
            files = parentFile.listFiles();
        } else {
            files = new File[1];
            files[0] = parentFile;
        }
        String pathName;
        InputStream is = null;
        BufferedInputStream bis = null;
        byte[] cache = new byte[1024];
        for (File file : files) {
            if (file.isDirectory()) {
                pathName = file.getPath().substring(basePath.length() + 1) + File.separator;
                zos.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length() + 1);
                try {
                    is = new FileInputStream(file);
                    bis = new BufferedInputStream(is);
                    zos.putNextEntry(new ZipEntry(pathName));
                    int nRead = 0;
                    while ((nRead = bis.read(cache, 0, 1024)) != -1) {
                        zos.write(cache, 0, nRead);
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    IOUtils.closeQuietly(bis);
                    IOUtils.closeQuietly(is);
                }
            }
        }
    }

}
