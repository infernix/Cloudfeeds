package net.cloudfeeds.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.cloudfeeds.feedback.Feedback;
import net.cloudfeeds.helper.CloudfeedsHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/*")
public class FeedbackController {

	@Autowired
	private Validator validator;

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@RequestMapping(value = "/feedback", method = RequestMethod.GET)
	public String createFeedback(Model model,
			@RequestParam(defaultValue = "false") boolean raw) {
		model.addAttribute(new Feedback());

		return getView(raw,false,model);

	}

	@RequestMapping(value = "/feedback", method = RequestMethod.POST)
	public String recieveFeedback(@ModelAttribute Feedback feedback, BindingResult result,@RequestParam(defaultValue = "false") boolean raw,Model model)
			throws IOException {

		validator.validate(feedback, result);
		if(!result.hasErrors())CloudfeedsHelper.sendFeedback(feedback);
		return getView(raw,!result.hasErrors(),model);

	}
	
	
	private String getView(boolean raw, boolean success,Model model)
	{

		model.addAttribute("success", success);

		if (raw) {

			return "feedback.raw";
		} else {
			return "feedback";
		}
	}

}
