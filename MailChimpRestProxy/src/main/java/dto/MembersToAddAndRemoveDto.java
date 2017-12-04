package dto;

import java.util.Arrays;

public class MembersToAddAndRemoveDto {
	private String[] members_to_add;
	private String[] members_to_remove;
	
	public MembersToAddAndRemoveDto() {
	
	}

	public String[] getMembers_to_add() {
		return members_to_add;
	}

	public void setMembers_to_add(String[] members_to_add) {
		this.members_to_add = members_to_add;
	}

	public String[] getMembers_to_remove() {
		return members_to_remove;
	}

	public void setMembers_to_remove(String[] members_to_remove) {
		this.members_to_remove = members_to_remove;
	}

	@Override
	public String toString() {
		return "MembersToAddAndRemoveDto [members_to_add=" + Arrays.toString(members_to_add) + ", members_to_remove="
				+ Arrays.toString(members_to_remove) + "]";
	}
	
}
