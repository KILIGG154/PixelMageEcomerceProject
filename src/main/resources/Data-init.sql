-- 1. Insert Spreads (Các Trải Bài)
INSERT INTO spreads (name, description, position_count, min_cards_required) VALUES
(N'Một Lá (1-Card Draw)', N'Câu hỏi đơn giản, thông điệp ngày, định hướng nhanh', 1, 1),
(N'Ba Lá (3-Card Spread)', N'Quá Khứ - Hiện Tại - Tương Lai', 3, 3),
(N'Celtic Cross (Thập Tự Celt)', N'Trải bài 10 lá cung cấp một cái nhìn toàn cảnh về mọi khía cạnh của vấn đề.', 10, 10),
(N'Tình Yêu (Relationship Spread)', N'Đánh giá cấu trúc, ưu nhược điểm và tương lai của một mối quan hệ.', 7, 7);

-- 2. Insert Card Templates (78 lá bài mẫu)
-- Để insert identity
SET IDENTITY_INSERT card_templates ON;

INSERT INTO card_templates (card_template_id, name, description, design_path, arcana_type, is_active, created_at, updated_at) VALUES
(1, N'The Fool', N'Khởi đầu mới, tự do, mạo hiểm. Một thanh niên trẻ bước đến bờ vực của vách núi...', N'/assets/cards/major/the-fool.webp', 'Major', 1, GETDATE(), GETDATE()),
(2, N'The Magician', N'Ý chí, sức mạnh, hiện thực hoá. Người đàn ông chỉ tay lên trời, một tay chỉ xuống đất...', N'/assets/cards/major/the-magician.webp', 'Major', 1, GETDATE(), GETDATE()),
(3, N'The High Priestess', N'Trực giác, bí ẩn, vô thức. Nữ tư tế ngồi giữa hai dãy cột B và J...', N'/assets/cards/major/the-high-priestess.webp', 'Major', 1, GETDATE(), GETDATE());
-- (Trong thực tế cần bộ Data đầy đủ cho 78 lá, ở đây insert tượng trưng cho MVP Data-init)

SET IDENTITY_INSERT card_templates OFF;

-- 3. Insert Divine Helpers (Bảo bối chứa keys cho Tarot AI System)
SET IDENTITY_INSERT divine_helpers ON;

INSERT INTO divine_helpers (divine_helper_id, card_template_id, upright_meaning, reversed_meaning, zodiac_sign, element, keywords, created_at, updated_at) VALUES
(1, 1, N'Bắt tay vào một dự án mới, sự ngây thơ, sự tự do vô lượng', N'Sự liều lĩnh, thiếu suy nghĩ, rủi ro khôn lường', N'Bạch Dương', N'Không Khí (Air)', N'Khởi đầu, Tự do, Mạo hiểm, Niềm tin', GETDATE(), GETDATE()),
(2, 2, N'Khả năng kiểm soát tình huống, sức mạnh ý chí, tài năng dồi dào', N'Thao túng, ảo tưởng, tài năng không được bộc lộ', N'Song Tử', N'Không Khí (Air)', N'Ý chí, Sức mạnh, Biến đổi, Tiềm năng', GETDATE(), GETDATE()),
(3, 3, N'Sự tĩnh lặng bề trong, tiếng nói trực giác mạnh mẽ, bí ẩn chưa giải đáp', N'Bí mật đen tối, mất kết nối với bản ngã, bị kìm nén cảm xúc', N'Cự Giải', N'Nước (Water)', N'Trực giác, Bí ẩn, Vô thức', GETDATE(), GETDATE());

SET IDENTITY_INSERT divine_helpers OFF;
