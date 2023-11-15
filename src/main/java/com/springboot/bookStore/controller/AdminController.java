package com.springboot.bookStore.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import com.springboot.bookStore.entity.Orders;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.bookStore.entity.Book;
import com.springboot.bookStore.entity.Order_details;
import com.springboot.bookStore.helper.Message;
import com.springboot.bookStore.repository.BookRepository;
import com.springboot.bookStore.repository.OrderDetailsRepository;
import com.springboot.bookStore.repository.OrderRepository;
import com.springboot.bookStore.service.BookService;

@RequestMapping("/admin")
@Controller
public class AdminController {
	@Autowired
	private BookService bookService;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private OrderDetailsRepository orderDetailsRepo;

	@RequestMapping("/books/create")
	public String showBookCreatePage() {
		return "addBook";
	}

	@ModelAttribute
	public void addAttributes(Model model) {
		List<String> categories = bookRepository.findByCategoryGroup();
		model.addAttribute("categories", categories);
	}

	public String generateImgName(MultipartFile file, String s) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String str = timestamp.toString();

		String str1 = str.replace(":", "_").replace(" ", "_");
		String filename = file.getOriginalFilename();
		int indexOf = filename.lastIndexOf(".");

		s = filename.substring(0, indexOf) + str1 + filename.substring(indexOf);
		return s;
	}

	public void StoreInImageFolder(String s, MultipartFile file) {
		File saveFile;
		try {
			System.out.println(new ClassPathResource("static/img"));
			saveFile = new ClassPathResource("static/img").getFile();
			System.out.println(saveFile);
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + s);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("/books/{page}")
	public String getAllBooks(@PathVariable("page") int page, Model model) {
		Pageable pageable = PageRequest.of(page, 9);
		Page<Book> books = bookService.getAllBooks(pageable);
		model.addAttribute("books", books);
		List<Book> a = new ArrayList<>();
		List<Book> b = new ArrayList<>();
		List<Book> c = new ArrayList<>();
		int j = 1;
		for (Book var : books) {
			if (j <= 3) {
				a.add(var);
				j = j + 1;
			} else if (j >= 4 && j <= 6) {
				b.add(var);
				j = j + 1;
			} else {
				c.add(var);
				j = j + 1;
			}
		}
		model.addAttribute("books", books);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", books.getTotalPages());

		model.addAttribute("List1", a);
		model.addAttribute("List2", b);
		model.addAttribute("List3", c);

		return "Admin/show-books";
	}

	@RequestMapping("/books/{category}/{page}")
	public String getBookByCategory(@PathVariable("page") int page, @PathVariable("category") String category,
			Model model) {
		Pageable pageable = PageRequest.of(page, 9);
		Page<Book> books = bookRepository.findByCategory(pageable, category);
		model.addAttribute("books", books);
		List<Book> a = new ArrayList<>();
		List<Book> b = new ArrayList<>();
		List<Book> c = new ArrayList<>();
		int j = 1;
		for (Book var : books) {
			if (j <= 3) {
				a.add(var);
				j = j + 1;
			} else if (j >= 4 && j <= 6) {
				b.add(var);
				j = j + 1;
			} else {
				c.add(var);
				j = j + 1;
			}
		}
		model.addAttribute("books", books);
		model.addAttribute("category", category);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", books.getTotalPages());

		model.addAttribute("List1", a);
		model.addAttribute("List2", b);
		model.addAttribute("List3", c);

		return "Admin/show-book-by-category";
	}

	@RequestMapping("/books/{id}/book")
	public String getBookById(@PathVariable("id") long id, Model model) {
		Book book = bookService.getBookById(id);
		model.addAttribute("book", book);
		return "Admin/Book_details";
	}

	@PostMapping("/process-book")
	public String processBook(@ModelAttribute Book book, HttpSession session,
			@RequestParam("bookImage") MultipartFile file) {
		String error_msg = "Something went wrong. Please try Again Later";
		try {

			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			if (fileName.contains("..")) {
				System.out.println("not a a valid file");

			}
			System.out.println("file name " + fileName);

			try {
				book.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
			} catch (IOException e) {
				System.out.println("Error");
				e.printStackTrace();
			}
			bookRepository.save(book);
			session.setAttribute("message", new Message("Your Book is added successfully", "success"));

		} catch (Exception e) {
			session.setAttribute("message", new Message(error_msg, "danger"));
			e.printStackTrace();
		}
		return "addBook";
	}

	@RequestMapping("/books/update/{id}")
	public String showBookCreatePage(@PathVariable("id") long id, Model model) {
		Book book = bookRepository.findById(id).get();
		model.addAttribute("book", book);
		return "updateBook";
	}

	@PostMapping(value = "/update-book")
	public String ProcessUpdateBook(@ModelAttribute Book book, HttpSession session,
			@RequestParam("bookImage") MultipartFile file) {
		try {

			Book oldBookDetails = bookRepository.findById(book.getId()).get();
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			if (fileName.contains("..")) {
				System.out.println("not a a valid file");

			}
			if(file.isEmpty()) {
				book.setImage(oldBookDetails.getImage());
			}
			
			else {
				try {
					book.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
				} catch (IOException e) {
					System.out.println("Error");
					e.printStackTrace();
				}
			}
			bookRepository.save(book);
			session.setAttribute("message", new Message("Your Book is updated ...", "success"));
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong", "danger"));

			e.printStackTrace();
		}

		return "redirect:/admin/books/0";
	}

	@RequestMapping("/books/delete/{id}")
	@ResponseBody
	public String deleteBookById(@PathVariable("id") long id) {
		bookService.deleteById(id);
		return "Book deleted successfully with id " + id;
	}

	@RequestMapping("/orders/{page}")
	public String Orders(@PathVariable("page") int page, Model model) {
		Pageable pageable = PageRequest.of(page, 5);
		Page<Orders> orders = orderRepo.findAll(pageable);
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", orders.getTotalPages());

		return "Admin/Display_orders";
	}

	@RequestMapping("/orders/id/{id}")
	public String OrderDetailsById(@PathVariable("id") long id, Model model) {
		List<Order_details> orders_details = orderDetailsRepo.findByOrdersId(id);
		model.addAttribute("orders_details", orders_details);
		model.addAttribute("order_id", id);
		return "Admin/Order_Details";
	}

	@RequestMapping("/orders/request/{page}")
	public String OrdersRequest(@PathVariable("page") int page, Model model) {
		Pageable pageable = PageRequest.of(page, 5);
		String s = "Order Request Received";
		Page<com.springboot.bookStore.entity.Orders> orders = orderRepo.findByOrder_status(s, pageable);

		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", orders.getTotalPages());
		model.addAttribute("status", "request/");
		return "Admin/Display_orders";
	}

	@RequestMapping("/orders/process/{page}")
	public String OrdersProgress(@PathVariable("page") int page, Model model) {
		Pageable pageable = PageRequest.of(page, 5);
		String s = "Order Processing";
		Page<com.springboot.bookStore.entity.Orders> orders = orderRepo.findByOrder_status(s, pageable);
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", orders.getTotalPages());
		model.addAttribute("status", "process/");
		return "Admin/Display_orders";
	}

	@RequestMapping("/orders/delivered/{page}")
	public String OrdersDeliver(@PathVariable("page") int page, Model model) {
		Pageable pageable = PageRequest.of(page, 5);
		String s = "Order Delivered";
		Page<com.springboot.bookStore.entity.Orders> orders = orderRepo.findByOrder_status(s, pageable);
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", orders.getTotalPages());
		model.addAttribute("status", "delivered/");
		return "Admin/Display_orders";
	}

	@RequestMapping("/orders/{id}/update")
	public String OrderStatusUpdateById(@PathVariable("id") long id, Model model) {
		model.addAttribute("Oid", id);
		return "Admin/Update_order_form";
	}

	@RequestMapping("/orders/statusUpdate")
	public String OrderProcess(@RequestParam("order_status") String order_status, HttpSession session,
			@RequestParam("id") long id) {

		com.springboot.bookStore.entity.Orders orders = orderRepo.findById(id).get();
		orders.setOrder_status(order_status);
		orderRepo.save(orders);
		session.setAttribute("message", new Message("Successfully Updated", "success"));
		return "redirect:/admin/orders/0";
	}

	@RequestMapping("/orders/{id}/updateStatus")
	public String OrderProductStatusUpdateById(@PathVariable("id") long id, Model model) {
		Order_details order_details = orderDetailsRepo.findById(id).get();
		long order_id = order_details.getOrders().getId();

		model.addAttribute("oid", id);
		model.addAttribute("order_id", order_id);
		model.addAttribute("order_update", order_details.getOrderUpdate());

		return "Admin/Update_order_details_form";
	}

	@RequestMapping(value = "/orders/statusUpdateProcess")
	public String OrderStatusUpdateProcess(@ModelAttribute Order_details order_details, HttpSession session,
			@RequestParam("order_id") long order_id) {
		long id = order_details.getId();
		System.out.println();
		Order_details details = orderDetailsRepo.findById(id).get();
		com.springboot.bookStore.entity.Orders orders = orderRepo.findById(order_id).get();

		details.setOrders(orders);
		details.setOrderStatus(order_details.getOrderStatus());
		details.setOrderUpdate(order_details.getOrderUpdate());
		orderDetailsRepo.save(details);

		session.setAttribute("message", new Message("Successfully Updated", "success"));
		return "redirect:/admin/orders/0";
	}

}
