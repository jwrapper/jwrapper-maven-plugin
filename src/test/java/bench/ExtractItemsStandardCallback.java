package bench;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

public class ExtractItemsStandardCallback {

	public static class MyExtractCallback implements IArchiveExtractCallback {

		private int hash = 0;
		private int size = 0;
		private int index;
		private boolean skipExtraction;
		private final ISevenZipInArchive inArchive;

		private final File folder;

		public MyExtractCallback(final ISevenZipInArchive inArchive,
				final File folder) {
			this.inArchive = inArchive;
			this.folder = folder;
		}

		@Override
		public ISequentialOutStream getStream(final int index,
				final ExtractAskMode extractAskMode) throws SevenZipException {

			this.index = index;

			skipExtraction = (Boolean) inArchive.getProperty(index,
					PropID.IS_FOLDER);

			if (skipExtraction || extractAskMode != ExtractAskMode.EXTRACT) {
				return null;
			}

			final String path = (String) inArchive.getProperty(index,
					PropID.PATH);
			System.err.println("Path: " + path);

			folder.mkdirs();

			final FileOutputStream output;
			try {
				output = new FileOutputStream(new File(folder, path));
			} catch (final Exception e) {
				throw new SevenZipException(e);
			}

			return new ISequentialOutStream() {
				@Override
				public int write(final byte[] data) throws SevenZipException {
					hash ^= Arrays.hashCode(data);
					size += data.length;
					try {
						output.write(data);
					} catch (final Exception e) {
						throw new SevenZipException(e);
					}
					return data.length; // Return amount of proceed data
				}
			};

		}

		@Override
		public void prepareOperation(final ExtractAskMode extractAskMode)
				throws SevenZipException {
		}

		@Override
		public void setOperationResult(
				final ExtractOperationResult extractOperationResult)
				throws SevenZipException {

			if (skipExtraction) {
				return;
			}

			if (extractOperationResult != ExtractOperationResult.OK) {
				System.err.println("Extraction error");
			} else {
				System.out.println(String.format("%9X | %10s | %s", hash, size,//
						inArchive.getProperty(index, PropID.PATH)));
				hash = 0;
				size = 0;
			}
		}

		@Override
		public void setCompleted(final long completeValue)
				throws SevenZipException {
		}

		@Override
		public void setTotal(final long total) throws SevenZipException {
		}
	}

	public static void main(final String[] args) {

		final String path = "/tmp/oracle/jre-6u34-windows-i586.exe";

		RandomAccessFile randomAccessFile = null;

		ISevenZipInArchive inArchive = null;

		try {

			randomAccessFile = new RandomAccessFile(path, "r");

			inArchive = SevenZip.openInArchive(null, // autodetect archive type
					new RandomAccessFileInStream(randomAccessFile));

			System.out.println("   Hash   |    Size    | Filename");
			System.out.println("----------+------------+---------");

			final int[] in = new int[inArchive.getNumberOfItems()];
			for (int i = 0; i < in.length; i++) {
				in[i] = i;
			}

			inArchive.extract(in, false, // Non-test mode
					new MyExtractCallback(inArchive, new File(
							"./target/extract")));

		} catch (final Exception e) {
			System.err.println("Error occurs: " + e);
			System.exit(1);
		} finally {
			if (inArchive != null) {
				try {
					inArchive.close();
				} catch (final Exception e) {
					System.err.println("Error closing archive: " + e);
				}
			}
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (final IOException e) {
					System.err.println("Error closing file: " + e);
				}
			}
		}
	}
}