package com.smart.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import java.nio.file.Path;


import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import com.smart.Entities.Contact;
import com.smart.Entities.User;
import com.smart.helper.Message;

import com.smart.Dao.ContactRepository;
import com.smart.Dao.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	ContactRepository contactrepository;

	@Autowired
	private BCryptPasswordEncoder encodedpass;
	
	@ModelAttribute
	public void Commondata(Model model,Principal principal)
	{
		  String username=principal.getName();
		  
		  User user=repository.getUserByUserName(username);
		  
		  model.addAttribute("user",user);
	}
	
	 @RequestMapping("/index")
	  public String dashboard(Model model,Principal principal){
		  
		 model.addAttribute("title","user dashboard");
		  return "normal/user_dashboard";
		  
	  }
	  
	  @GetMapping("/add-contact")
	  public String openAddContactForm(Model model,Principal principal)
	  {
		  
		  model.addAttribute("title","add-contact");
		  model.addAttribute("contact",new Contact());
		 
		  return"normal/add_contact_form";
	  }
	  
	  @PostMapping("/process-contact")
	  public String processContact(@ModelAttribute Contact contact,
			  @RequestParam("profileImage")MultipartFile file,
			  Principal principal,HttpSession session)
	  {
		  try {
		  String name=principal.getName();
		  
		  User user=this.repository.getUserByUserName(name);
		  
		  
		  if(file.isEmpty())
		  {
			  contact.setImage("contact.png");
		  }
		  else {
			  
			  contact.setImage(file.getOriginalFilename());
			  
			  File saveFile=new ClassPathResource("static/img").getFile();
			  
			  Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			  
			  Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			  session.setAttribute("message",new Message("Contact Uploaded Successfully","success"));
			  
		
		  }
		  
		  user.getContacts().add(contact);
		  contact.setUser(user);
		  
		  this.repository.save(user);
		  
		  }
		  catch(Exception e)
		  {
			  System.out.println(e);
			  session.setAttribute("message",new Message("Something went wrong! Please try again..! ","danger"));
			  
		  }
		  return"normal/add_contact_form";
	  }
	  
	  @RequestMapping("/show-contacts/{page}")
	  public String showContacts(@PathVariable("page")Integer page,Model model,Principal principal)
	  {
		  model.addAttribute("title", "view-contacts");
		  
		  String username=principal.getName();
		  User user=this.repository.getUserByUserName(username);
		  
		  Pageable pageable=PageRequest.of(page, 3);
		  
		  Page<Contact> contacts =this.contactrepository.findContactsByUser(user.getId(),pageable);
		  
		  model.addAttribute("contacts", contacts);
		  model.addAttribute("currentpage", page);
		  model.addAttribute("Totalpages", contacts.getTotalPages());
		 
		  
		  return "normal/show_contacts";
	  }
	  
	  @RequestMapping("/{cId}/contact-info")
	  public String contactInfo(@PathVariable("cId") Integer cId,Model model,Principal principal)
	  {
		  Optional<Contact>Optionalcontact=this.contactrepository.findById(cId);
		  Contact contact=Optionalcontact.get();
		  
		  String username=principal.getName();
		  
		  User user=this.repository.getUserByUserName(username);
		  
		  if(user.getId()==contact.getUser().getId())
		  {
			  model.addAttribute("contact", contact);
			  model.addAttribute("title","User-Detail");
		  }
				  
				  
		 
		  return "normal/contact-info";
	  }
	  
	  @GetMapping("/delete-contact/{cId}")
	  public String Delete(@PathVariable("cId")Integer cId,HttpSession session,Principal principal)
	  {
		  Contact contact=this.contactrepository.findById(cId).get();
		  
		  
		  User user=this.repository.getUserByUserName(principal.getName());
		  user.getContacts().remove(contact);
		  
		  this.repository.save(user);
		 
		
		  session.setAttribute("message",new Message("Contact Deleted Successfully","success"));
		  
		  return "redirect:/user/show-contacts/0";
		  
	  }
	  
	  @PostMapping("/update-contact/{cid}")
	  public String updateForm(Model model,@PathVariable("cid")Integer cid)
	  {
		  
		  model.addAttribute("title","update-contact");
		  
		  Contact contact=this.contactrepository.findById(cid).get();
		  
		  model.addAttribute("contact", contact);
		  
		  return "normal/update_form";
		  
	  }
	  
	  @PostMapping("/process-update")
	  public String update(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file,Principal principal,HttpSession session)
	  {	  
		  
		  try {
			  
			  		Contact oldContactDetail = this.contactrepository.findById(contact.getcId()).get();
			  
			  		if(!file.isEmpty())
			  		{
			  			
			  			 //Delete ExistingPhoto
			  			
			  			  File delfile=new ClassPathResource("static/img").getFile();
			  			  File f2=new File(delfile,oldContactDetail.getImage());
			  			  f2.delete();
			  			
			  			  //Upload New Photo
						  
						  File saveFile=new ClassPathResource("static/img").getFile();
						  
						  Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
						  
						  Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
						  
						  contact.setImage(file.getOriginalFilename());
			  			
			  			
			  		}
			  		else {
			  			
			  			oldContactDetail.setImage(oldContactDetail.getImage());
			  		}
			  		
			  		
			  		
			  		User user=this.repository.getUserByUserName(principal.getName());
			  		contact.setUser(user);
			  		
			  		this.contactrepository.save(contact);
			  		
			  		
			  		session.setAttribute("message", new Message("Your Contact is Updated Successfully","success"));
			  		
			  
		  }catch (Exception e) {
			// TODO: handle exception
		}
		  
		  
		  
		  
		  return "redirect:/user/"+contact.getcId()+"/contact-info/";
	  }
	  
	  
	  @GetMapping("/profile")
	  public String profile(Model model)
	  {
		  
		  model.addAttribute("title", "Your Profile");
		  
		  
		  
		  return "normal/profile";
		  
	  }
	  
	  @RequestMapping("/settings")
	  public String openSettings(Model model)
	  {
		  
		  model.addAttribute("title","settings");
		
		  return "normal/settings";
	  }
	  
	  @PostMapping("/change-password")
	  public String changePassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,Principal principal,HttpSession session)
	  {
		  
		  User user=this.repository.getUserByUserName(principal.getName());
		  
		  
		  
		  if(this.encodedpass.matches(oldPassword, user.getPassword())) {
			  
			  user.setPassword(this.encodedpass.encode(newPassword));
			  this.repository.save(user);
			  session.setAttribute("message", new Message("Your password has been changed successfully","success"));
			  
			  
		  }
		  else {
			  
			  session.setAttribute("message", new Message("Please enter correct old password","danger"));
			  return "redirect:/user/settings";
			  
		  }
		  
		  return "redirect:/user/index";
	  }
	  
}






