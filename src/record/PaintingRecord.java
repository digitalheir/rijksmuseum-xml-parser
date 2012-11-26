package record;

import record.header.Header;
import record.metadata.MetaDatas;
import record.metadata.Metadata;

public class PaintingRecord {
	public PaintingRecord(Header header, Metadata metadata) {
		super();
		this.header = header;
		this.metadata = metadata;
	}
	private Header header;
	private Metadata metadata;
	
	public Header getHeader() {
		return header;
	}
	
	public Metadata getMetadata() {
		return metadata;
	}

	public String[] getMetadatas(MetaDatas md) {
		return this.metadata.getMetadatas(md);
	}
}
