package com.daar_projet_cloud.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import model.Book;
import repository.bookRepository;


@Controller // This means that this class is a Controller

public class DemoController {
  @Autowired
  private bookRepository userRepository;

  
  /*
   * for test :
   * $ curl localhost:8181/demo/add -d name=bookname -d email=someone
   */
  
  
  @PostMapping(path="/add") // Map ONLY POST Requests
  public @ResponseBody String addNewUser (@RequestParam String name
      , @RequestParam String email) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request

    Book n = new Book();
    n.setName(name);
  
    userRepository.save(n);
    return "Saved";
  }

  
  
  /*
   * for test :
   * localhost:8181/demo/all
   */
  
  @GetMapping(path="/all")
  public @ResponseBody Iterable<Book> getAllUsers() {
    // This returns a JSON or XML with the users
    return userRepository.findAll();
  }
  

  /*
   * for test :
   * localhost:8181/search
   */  
  @PostMapping(path="/search")
  public String search(@RequestParam(name="regex", required=false, defaultValue="Worlddd") String regex, Model model) {
      model.addAttribute("regex", regex);
      return "search_result";
  }

  
}
