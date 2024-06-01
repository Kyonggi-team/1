package security.loginsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import security.loginsecurity.dto.MemberDTO;
import security.loginsecurity.member.Member;
import security.loginsecurity.repository.MemberRepository;
import security.loginsecurity.service.MemberService;


@Controller
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public MemberController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }


    @PostMapping("/member")
    public String signup(MemberDTO memberDTO){
        memberService.save(memberDTO);
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
    @GetMapping("/myPage")
    public String myPage(){
        return "myPage";
    }
    @GetMapping("/forgot_password")
    public String rePassword(){
        return "reset_password";
    }


    //회원업데이트
    @GetMapping("/edit-profile")
    public String showEditProfileForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Correctly fetch the email now

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with the email: " + email));

        // Prepare your model attributes here...
        model.addAttribute("member", member);

        return "edit-profile";
    }
    //회원 업데이트
    @PostMapping("/update-profile")
    public String updateProfile(MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/login";
    }
    //회원삭제
    @PostMapping("/delete-account")
    public String deleteAccount(Authentication authentication) {
        String email = authentication.getName();
        memberService.deleteByEmail(email);
        return "redirect:/login?logout";
    }

}