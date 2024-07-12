package gift.service;

import gift.repository.WishlistRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.model.Wishlist;
import gift.model.Member;
import gift.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, MemberRepository memberRepository,
                           ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Page<Wishlist> getWishlist(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        return wishlistRepository.findByMemberId(member.getId(), pageable);
    }

    public void addProductToWishlist(String email, Long productId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        Wishlist wishlist = new Wishlist(member, product);
        wishlistRepository.save(wishlist);
    }

    @Transactional
    public void removeProductFromWishlist(String email, Long productId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        wishlistRepository.deleteByMemberIdAndProductId(member.getId(), productId);
    }
}
