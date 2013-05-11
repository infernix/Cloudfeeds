package net.cloudfeeds.feedback;

import net.cloudfeeds.service.APIService;

import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Length;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

public class Feedback {

	@NotBlank  
	@Length(max = 80)
	private String name;
	
	@NotBlank
    @Email
    @Length(max = 80)
	private String email;
	
	@NotBlank  
	@Length(max = 80)
	private String title;
	
	@NotBlank
    @Length(max = 4000)
	private String feedback;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String sep = APIService.LINE_SEPARATOR;
		builder.append("Name: " + name);
		builder.append(sep);
		builder.append("Email: " + email);
		builder.append(sep);
		builder.append("Title: " + title);
		builder.append(sep);
		builder.append("Feedback: " + feedback);
		builder.append(sep);
		return builder.toString();
	}
	
	
}
