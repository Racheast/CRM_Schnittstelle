package dto;

public class GroupDto {
	private int id;
	private String name;
	private long stamp;
	private long last_mailing;
	private long last_changed;
	private boolean isLocked;
	
	public GroupDto() {
		
	}
	
	public GroupDto(int id, String name, long stamp, long last_mailing, long last_changed, boolean isLocked) {
		super();
		this.id = id;
		this.name = name;
		this.stamp = stamp;
		this.last_mailing = last_mailing;
		this.last_changed = last_changed;
		this.isLocked = isLocked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getStamp() {
		return stamp;
	}

	public void setStamp(long stamp) {
		this.stamp = stamp;
	}

	public long getLast_mailing() {
		return last_mailing;
	}

	public void setLast_mailing(long last_mailing) {
		this.last_mailing = last_mailing;
	}

	public long getLast_changed() {
		return last_changed;
	}

	public void setLast_changed(long last_changed) {
		this.last_changed = last_changed;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	@Override
	public String toString() {
		return "GroupDto [id=" + id + ", name=" + name + ", stamp=" + stamp + ", last_mailing=" + last_mailing
				+ ", last_changed=" + last_changed + ", isLocked=" + isLocked + "]";
	}
	
	
	
}
