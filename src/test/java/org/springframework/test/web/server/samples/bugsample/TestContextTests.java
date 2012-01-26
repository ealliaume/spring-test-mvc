package org.springframework.test.web.server.samples.bugsample;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


public class TestContextTests {

	@Test
	public void should_do_the_post() throws Exception {
		standaloneSetup(new SimpleController()).build()
			.perform(post("/bug/EXISTING"))
				.andExpect(status().isOk());
	}

	@Test
	public void should_do_the_get_without_the_same_url() throws Exception {
		standaloneSetup(new SimpleController()).build()
			.perform(get("/bug/EXIT"))
				.andExpect(status().isOk());

	}

	@Test
	public void should_do_the_get_with_the_same_url_than_the_post() throws Exception {
		standaloneSetup(new SimpleController()).build()
			.perform(get("/bug/EXISTING"))
				.andExpect(status().isOk()); // ! BUG, we have a 403 !

	}

	@Controller
	@SuppressWarnings("unused")
	private static class SimpleController {

		@RequestMapping(value = "/bug/{type}", method = GET)
		@ResponseBody
		public String bugGet(@PathVariable("type") final String type) {
			return "GET with : " + type;
		}

		@RequestMapping(value = "/bug/EXISTING", method = POST)
		@ResponseBody
		public String postMakingTheBug() {
			return "POST";
		}
	}

}
