package security.loginsecurity.service;


import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.loginsecurity.dto.MemberDTO;
import security.loginsecurity.member.Member;
import security.loginsecurity.repository.MemberRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final String defaultProfilePictureUrl ="sunny.jpeg";


    //회원가입
    public void save(MemberDTO memberDTO){
        memberRepository.save(Member.builder()
                .email(memberDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(memberDTO.getPassword()))
                .name(memberDTO.getName())
                .phoneNumber(memberDTO.getPhoneNumber())
                .build());

    }


    //회원정보 수정
    public void update(MemberDTO memberDTO) {
        Member member = memberRepository.findByEmail(memberDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("No user found with the email: " + memberDTO.getEmail()));

        // 업데이트 로직
        if (memberDTO.getName() != null && !memberDTO.getName().isEmpty()) {
            member.updateName(memberDTO.getName());
        }
        if (memberDTO.getPhoneNumber() != null && !memberDTO.getPhoneNumber().isEmpty()) {
            member.updatePhoneNumber(memberDTO.getPhoneNumber());
        }
        if (memberDTO.getPassword() != null && !memberDTO.getPassword().isEmpty()) {
            member.updatePassword(memberDTO.getPassword(), bCryptPasswordEncoder);
        }

        memberRepository.save(member); // 변경된 정보 저장
    }



    //회원탈퇴
    @Transactional
    public void deleteByEmail(String email) {
        memberRepository.deleteByEmail(email);
    }


    //password찾을때 이메일이 존재하는지 확인
    public boolean existsByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    //password 임시비밀번호로 재설정

    public void updatePassword(String email, String newPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 이메일입니다: " + email));
        member.setPassword(newPassword);  // 암호화된 비밀번호 설정
        memberRepository.save(member);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with the email: " + email));
    }




    ////cnrk
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No user found with the id: " + id));
    }
}