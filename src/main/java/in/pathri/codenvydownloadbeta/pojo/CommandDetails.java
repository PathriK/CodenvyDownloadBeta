package in.pathri.codenvydownloadbeta.pojo;
// import java.util.List;

public class CommandDetails {
	public String commandLine;
	public String name;
	public String type;
	
	public CommandDetails(String newCommandLine, String name, String type) {
		this.name = name;
		this.commandLine = newCommandLine;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "{name:" + this.name + "}";	
	}

	public CommandDetails getInstance(String project) {
		String newCommandLine = commandLine.replace("${current.project.path}", "/projects/" + project);
		return new CommandDetails(newCommandLine, name, type);
	}
}