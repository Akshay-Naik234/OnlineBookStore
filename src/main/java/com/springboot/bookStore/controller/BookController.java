package com.springboot.bookStore.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.springboot.bookStore.Constants.AppConstants;
import com.springboot.bookStore.entity.Address;
import com.springboot.bookStore.entity.Book;
import com.springboot.bookStore.entity.OrderPaymentDetails;
import com.springboot.bookStore.entity.Order_details;
import com.springboot.bookStore.entity.Orders;
import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.entity.cart_items;
import com.springboot.bookStore.helper.Message;
import com.springboot.bookStore.repository.AddressRepository;
import com.springboot.bookStore.repository.BookRepository;
import com.springboot.bookStore.repository.CartItemRepository;
import com.springboot.bookStore.repository.OrderDetailsRepository;
import com.springboot.bookStore.repository.OrderPaymentRepository;
import com.springboot.bookStore.repository.OrderRepository;
import com.springboot.bookStore.repository.UserRepository;
import com.springboot.bookStore.service.BookService;
import com.springboot.bookStore.service.Impl.EmailService;

@RequestMapping("/user")
@Controller
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private AdminController adminController;

	@Autowired
	private OrderPaymentRepository orderPaymentRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CartItemRepository cartRepo;

	@ModelAttribute
	public void addAttributes(Model model) {
		List<String> categories = bookRepository.findByCategoryGroup();
		model.addAttribute("categories", categories);
	}

	public boolean checkUserLoggedIn(Principal principal) {
		try {
			principal.getName();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@RequestMapping("/books/{page}")
	public String getAllBooks(@PathVariable("page") int page, Model model,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, 9, sort);
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

		String s = "&sortDir=" + sortDir;

		model.addAttribute("books", books);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", books.getTotalPages());

		model.addAttribute("List1", a);
		model.addAttribute("List2", b);
		model.addAttribute("List3", c);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("s", s);
		return "user/show-books";
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

		return "user/show-books-by-category";
	}

	@RequestMapping("/books/{id}/book")
	public String getBookById(@PathVariable("id") long id, Model model) {
		Book book = bookService.getBookById(id);
		model.addAttribute("book", book);
		return "user/Book_details";
	}

	@GetMapping("/show-change-password-form")
	public String showChangePasswordPage() {
		return "user/change_password_form";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, Principal principal) {

		if (!checkUserLoggedIn(principal)) {
			return "redirect:/signin";
		}
		String name = principal.getName();

		User user = userRepo.findByEmail(name).get();
		user.setPassword(passwordEncoder.encode(newpassword));
		userRepo.save(user);

		return "redirect:/signin?change=password changed successfully";
	}

	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		if (!checkUserLoggedIn(principal)) {
			return "redirect:/signin";
		}
		String name = principal.getName();

		User user = userRepo.findByEmail(name).get();
		model.addAttribute("user", user);
		return "user/profile";
	}

	public Orders processOrder(Model model, HttpSession session, Principal principal) {
		String name = principal.getName();
		User user = userRepo.findByEmail(name).get();
		List<cart_items> list_cart_items = cartRepo.findByUser(user);
		Address address = addressRepository.findByUser(user);

		float total = 0f;
		for (cart_items cart_item : list_cart_items) {
			total = total + (float) ((cart_item.getBook().getPrice()) * (cart_item.getQuantity()));
		}
		Orders orders = new Orders();
		orders.setAddress(address.getAddress());
		orders.setFirst_name(user.getName());
		orders.setCity(address.getCity());
		orders.setCountry(address.getCountry());
		orders.setState(address.getState());
		orders.setZipcode(address.getZipcode());

		orders.setEmail(name);
		orders.setPhone(user.getPhone());
		orders.setTotal(total);
		orders.setUser(user);
		orders.setOrder_status("Order Request Received");

		Orders orders2 = orderRepository.save(orders);

		for (cart_items cart_item : list_cart_items) {
			Order_details order_details = new Order_details();
			order_details.setBook(cart_item.getBook());
			order_details.setOrders(orders);

			order_details.setUnit_price((float) cart_item.getBook().getPrice());
			order_details.setQuantity(cart_item.getQuantity());
			float subTotal = (float) (cart_item.getBook().getPrice() * cart_item.getQuantity());
			order_details.setSubtotal(subTotal);
			order_details.setUnit_price((float) cart_item.getBook().getPrice());
			order_details.setOrderStatus("Order Requested");
			orderDetailsRepository.save(order_details);
		}
		return orders2;

	}

	@RequestMapping("/orders")
	public String ShowOrders(Principal principal, Model model) {
		if (!checkUserLoggedIn(principal)) {
			return "redirect:/signin";
		}
		String name = principal.getName();

		if (name != null) {
			User user = userRepo.findByEmail(name).get();
			List<Orders> orders = orderRepository.findByUser(user);

			model.addAttribute("orders", orders);
			return "user/show_orders";
		} else {
			return "Login first";
		}
	}

	@RequestMapping("/orders/{id}")
	public String OrderDetailsById(@PathVariable("id") long id, Model model) {
		List<Order_details> orders_details = orderDetailsRepository.findByOrdersId(id);
		model.addAttribute("orders_details", orders_details);
		model.addAttribute("order_id", id);
		return "user/Order_Details";
	}

	@GetMapping("/update-profile")
	public String updateProfilePage(Model model, Principal principal) {
		if (!checkUserLoggedIn(principal)) {
			return "redirect:/signin";
		}
		String name = principal.getName();

		User user = userRepo.findByEmail(name).get();
		model.addAttribute("user", user);
		return "user/update_profile";
	}

	@PostMapping(value = "/process-update-profile")
	public String ProcessUpdateProfile(@ModelAttribute User user, HttpSession session,
			@RequestParam("profileImage") MultipartFile file, Principal principal) {
		boolean logout = false;
		try {

			if (!checkUserLoggedIn(principal)) {
				return "redirect:/signin";
			}
			String name = principal.getName();

			User user1 = userRepo.findByEmail(name).get();
			if (user.getEmail().equals(user1.getEmail())) {
				logout = false;
			} else {
				logout = true;
			}
			user.setPassword(user1.getPassword());
			user.setRole(user1.getRole());

			String fileName = StringUtils.cleanPath(file.getOriginalFilename());

			if (file.isEmpty()) {
				user.setImage(user1.getImage());
			}

			else {
				try {
					user.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
				} catch (IOException e) {
					System.out.println("Error");
					e.printStackTrace();
				}
			}

			userRepo.save(user);
			session.setAttribute("message", new Message("Your Profile is updated ...", "alert-success"));
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong", "alert-danger"));

			e.printStackTrace();
		}
//		System.out.println("logout " + logout);
		if (logout) {
			session.setAttribute("message",
					new Message("Your email is updated Successfully,Please login again", "success"));
			return "redirect:/logout";
		}
		return "redirect:/user/profile";
	}

	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data, Principal principal) {
		float amt = Float.parseFloat(data.get("amount").toString());

		try {
			RazorpayClient client = new RazorpayClient("rzp_test_w9FS1rJrpea6jE", "HkeT4F12zeODYUJWe8lCnd8Z");

			JSONObject ob = new JSONObject();
			ob.put("amount", amt * 100);
			ob.put("currency", "INR");
			ob.put("receipt", "txn_234868");
			Order order = client.orders.create(ob);

			OrderPaymentDetails orderPaymentDetails = new OrderPaymentDetails();
			orderPaymentDetails.setAmount(order.get("amount") + "");
			orderPaymentDetails.setPaymentorderId(order.get("id"));
			orderPaymentDetails.setPaymentId(null);

			orderPaymentDetails.setStatus("created");
			orderPaymentDetails.setUser(userRepo.findByEmail(principal.getName()).get());
			orderPaymentDetails.setReceipt(order.get("receipt"));
			orderPaymentRepository.save(orderPaymentDetails);

			return order.toString();

		} catch (RazorpayException e) {
			e.printStackTrace();
			return "Failed";
		}

	}

	@PostMapping("/update_order")
	@Transactional
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data, Model model, HttpSession session,
			Principal principal) {
		OrderPaymentDetails orderPaymentDetails = orderPaymentRepository
				.findByPaymentorderId(data.get("order_id") + "");

		Object object2 = data.get("payment_id");
		orderPaymentDetails.setPaymentId(object2 + "");
		Object object = data.get("status");
		orderPaymentDetails.setStatus(object + "");

		String name = principal.getName();
		User user = userRepo.findByEmail(name).get();

		orderPaymentRepository.save(orderPaymentDetails);
		Orders order = processOrder(model, session, principal);
		long orderId = order.getId();

		cartRepo.deleteByUser(user);

		return ResponseEntity.ok(Map.of("msg", "updated"));
	}

	@GetMapping("/address")
	public String showAddressPage(Model model, Principal principal) {
		if (!checkUserLoggedIn(principal)) {
			return "redirect:/signin";
		}
		String name = principal.getName();
		User user = userRepo.findByEmail(name).get();
		Address address = addressRepository.findByUser(user);
		if (address != null) {
			model.addAttribute("update", "update");
			model.addAttribute("address", address);
			return "user/Address_form";
		}
		return "user/Address_form";
	}

	@PostMapping("/process-address")
	public String processAddress(@ModelAttribute Address address, @RequestParam("address1") String address1,
			Principal principal, HttpSession session) {
		if (!checkUserLoggedIn(principal)) {
			return "redirect:/signin";
		}
		String name = principal.getName();
		User user = userRepo.findByEmail(name).get();
		Address findByUser = addressRepository.findByUser(user);

		if (findByUser != null) {
			session.setAttribute("message", new Message("Address Already Exists", "alert-danger"));
			return "redirect:/user/books/0";
		}

		address.setUser(user);
		address.setAddress(address1);

		addressRepository.save(address);
		session.setAttribute("message", new Message("Successfully Added Address", "alert-success"));
		return "redirect:/user/books/0";
	}

	@GetMapping("/show-update-address")
	public String updateAdd(Model model, Principal principal, HttpSession session) {
		if (!checkUserLoggedIn(principal)) {
			return "redirect:/signin";
		}
		String name = principal.getName();
		User user = userRepo.findByEmail(name).get();
		Address address = addressRepository.findByUser(user);
		model.addAttribute(address);
		return "user/update_address";
	}

	@PostMapping("/process-update-address")
	public String updateAddress(@ModelAttribute Address address, Principal principal,
			@RequestParam("address1") String address1, HttpSession session) {
		if (!checkUserLoggedIn(principal)) {
			return "redirect:/signin";
		}
		String name = principal.getName();
		User user = userRepo.findByEmail(name).get();
		address.setUser(user);
		address.setAddress(address1);
		addressRepository.save(address);
		session.setAttribute("message", new Message("Address Successfully Updated ", "alert-success"));
		return "redirect:/user/address";
	}

}
